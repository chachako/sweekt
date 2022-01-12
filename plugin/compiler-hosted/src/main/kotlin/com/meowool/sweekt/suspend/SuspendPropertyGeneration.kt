/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
package com.meowool.sweekt.suspend

import com.meowool.sweekt.AbstractIrTransformer
import com.meowool.sweekt.ModuleLoweringPass
import com.meowool.sweekt.SweektNames.Suspend
import com.meowool.sweekt.SweektNames.realSuspendGetter
import com.meowool.sweekt.SweektNames.realSuspendSetter
import com.meowool.sweekt.SweektNames.suspendGetter
import com.meowool.sweekt.SweektNames.suspendSetter
import com.meowool.sweekt.castOrNull
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.DeepCopyIrTreeWithSymbols
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.DeepCopyTypeRemapper
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.Name

/**
 * @author 凛 (RinOrz)
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
class SuspendPropertyGeneration(
  private val pluginContext: IrPluginContext,
  private val configuration: CompilerConfiguration
) : ModuleLoweringPass {
  override fun lower(module: IrModuleFragment) {
    val suspendSymbols = mutableMapOf<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>()

    module.transformChildrenVoid(PropertiesCollector(pluginContext, suspendSymbols))

    val remapper = PropertiesRemapper(suspendSymbols)
    module.acceptVoid(remapper)
    module.transformChildrenVoid(PropertiesTransformer(remapper, suspendSymbols))
    module.patchDeclarationParents()
  }

  private inner class PropertiesCollector(
    context: IrPluginContext,
    private val oldToSuspend: MutableMap<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>,
  ) : AbstractIrTransformer(context, configuration) {
    override fun visitFunctionNew(declaration: IrFunction): IrStatement = declaration

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
      val property = super.visitPropertyNew(declaration) as IrProperty

      if (property.isFakeOverride || property.hasAnnotation(Suspend).not()) return property

      val parent = declaration.parentContainer
      val oldGetter = property.getter
      val oldSetter = property.setter

      // 'val field get()' -> fun suspend$get-field()
      oldGetter?.isolateAsSuspendFunction()?.transferCall()?.let { newFunction ->
        oldToSuspend[oldGetter.symbol] = newFunction.symbol
        parent.addChild(newFunction)
      }
      // 'val field set(value)' -> fun suspend$set-field(value: Any)
      oldSetter?.isolateAsSuspendFunction()?.transferCall()?.let { newFunction ->
        oldToSuspend[oldSetter.symbol] = newFunction.symbol
        parent.addChild(newFunction)
      }

      // Empty getter/setter
      return property.apply {
        getter = null
        setter = null
      }
    }

    private fun IrSimpleFunction.isolateAsSuspendFunction() = copy {
      isSuspend = true
      originalDeclaration = null
      origin = IrDeclarationOrigin.DEFINED
      name = Name.identifier("suspend\$${name.asStringStripSpecialMarkers()}")
    }.apply {
      correspondingPropertySymbol = null
    }

    private fun IrSimpleFunction.transferCall() = also { function ->
      function.body?.transformChildrenVoid(object : AbstractIrTransformer(pluginContext, configuration) {
        override fun visitCall(expression: IrCall): IrExpression {
          var call = super.visitCall(expression) as IrCall
          when (call.symbol.owner.kotlinFqName) {
            suspendGetter -> realSuspendGetter
            suspendSetter -> realSuspendSetter
            else -> null
          }?.also { replacedSymbol ->
            call = call.copy(symbol = pluginContext.referenceFunctions(replacedSymbol).single())
          }
          return call
        }
      })
      function.body?.statements?.singleOrNull()?.castOrNull<IrReturn>()?.also { returns ->
        function.body = IrBlockBodyImpl(function.startOffset, function.endOffset, listOf(returns.value))
      }
    }
  }

  private inner class PropertiesRemapper(
    private val oldToSuspend: MutableMap<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>
  ) : DeepCopySymbolRemapper() {
    override fun getReferencedSimpleFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
      oldToSuspend[symbol] ?: super.getReferencedSimpleFunction(symbol)

    override fun getReferencedFunction(symbol: IrFunctionSymbol): IrFunctionSymbol =
      oldToSuspend[symbol] ?: super.getReferencedFunction(symbol)
  }

  private inner class PropertiesTransformer(
    remapper: PropertiesRemapper,
    private val oldToSuspend: MutableMap<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>
  ) : DeepCopyIrTreeWithSymbols(remapper, DeepCopyTypeRemapper(remapper)) {
    override fun visitSimpleFunction(declaration: IrSimpleFunction): IrSimpleFunction =
      if (oldToSuspend.containsKey(declaration.symbol)) declaration else super.visitSimpleFunction(declaration)
  }
}
