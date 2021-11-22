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
@file:Suppress("NAME_SHADOWING")

package com.meowool.sweekt.lazyinit

import com.meowool.sweekt.AbstractIrTransformer
import com.meowool.sweekt.SweektNames.LazyInit
import com.meowool.sweekt.SweektNames.resetLazyValue
import com.meowool.sweekt.SweektNames.resetLazyValues
import com.meowool.sweekt.SweektSyntheticDeclarationOrigin
import com.meowool.sweekt.FinalFieldTransformer
import com.meowool.sweekt.cast
import com.meowool.sweekt.castOrNull
import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.ExpressionCodegen
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irComposite
import org.jetbrains.kotlin.ir.builders.irElseBranch
import org.jetbrains.kotlin.ir.builders.irEquals
import org.jetbrains.kotlin.ir.builders.irFalse
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.builders.irTrue
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.expressions.impl.IrGetObjectValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.DeepCopyTypeRemapper
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

/**
 * @author 凛 (https://github.com/RinOrz)
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
class LazyInitGeneration(private val configuration: CompilerConfiguration) : IrGenerationExtension {
  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    val finalProperties = mutableSetOf<IrProperty>()
    moduleFragment.transformChildrenVoid(Transformer(pluginContext, finalProperties))
    if (finalProperties.isNotEmpty()) {
      val symbolRemapper = DeepCopySymbolRemapper()
      val typeRemapper = DeepCopyTypeRemapper(symbolRemapper)
      moduleFragment.acceptVoid(symbolRemapper)
      moduleFragment.transformChildren(FinalFieldTransformer(finalProperties, symbolRemapper, typeRemapper), null)
      moduleFragment.patchDeclarationParents()
    }
  }

  private inner class Transformer(
    context: IrPluginContext,
    private val finalProperties: MutableSet<IrProperty>
  ) : AbstractIrTransformer(context, configuration) {
    private val propertiesOfIsInit = mutableMapOf<String, IrProperty>()

    /**
     * Gets `var _isInit$name = false` from `@LazyInit val name`
     */
    private fun IrProperty.getOrAddIsInitProperty(name: String) =
      propertiesOfIsInit.getOrPut(name) {
        parent.addProperty(
          name = "_isInit\$$name",
          original = this,
          type = irBuiltIns.booleanType,
          initializer = { irBoolean(false) }
        ) {
          visibility = DescriptorVisibilities.PUBLIC
        }
      }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
      fun default() = super.visitPropertyNew(declaration)
      if (declaration.isFakeOverride || declaration.hasAnnotation(LazyInit).not()) return default()
      val field = declaration.backingField ?: return default().apply {
        declaration.reportWarn("Property marked @LazyInit, but backingField is `null`: " + declaration.dump())
      }
      // val -> var
      if (declaration.isVar.not() || field.isFinal) {
        finalProperties.add(declaration)
      }

      // var _isInit$name = false
      val isInitValue = declaration.getOrAddIsInitProperty(declaration.name.asString())

      // get() = when {
      //   _isInit$xxx && xxx == null -> field
      //   else -> ???.also {
      //     field = it
      //     _isInit$xxx = true
      //   }
      // }
      declaration.getOrAddGetter().also { getter ->
        getter.origin = SweektSyntheticDeclarationOrigin
        getter.body = getter.buildIr {
          irReturnExprBody(
            irWhen(declaration.type) {
              +irBranch(
                irEquals(irGetProperty(getter, isInitValue), irTrue()),
                irGetField(getter, declaration)
              )
              +irElseBranch(
                irBlock {
                  val value = irTemporary(field.initializer!!.expression).apply {
                    parent = getter
                    transformCopiedDispatchReceiver(getter)
                  }
                  +irSetField(getter, declaration, irGet(value))
                  +irSetProperty(getter, isInitValue, irTrue())
                  +irGet(value)
                }
              )
            }
          )
        }
      }

      field.initializer = null
      return default()
    }

    override fun visitCall(expression: IrCall): IrExpression {
      val declaration = expression.symbol.owner
      var result: IrExpression = super.visitCall(expression)

      fun IrBuilderWithScope.resetInit(callProperty: IrCall): IrExpression? {
        // getAbc() ->
        //   @LazyInit val abc = ???
        val lazyProperty = callProperty.symbol.owner.correspondingPropertySymbol?.owner ?: return null
        // abc -> _isInit$abc
        val isInitValue = lazyProperty.getOrAddIsInitProperty(lazyProperty.name.asString())
        // _isInit$abc = false
        return irSetField(callProperty.dispatchReceiver, isInitValue, irFalse())
      }

      when (expression.symbol.descriptor.fqNameSafe) {
        resetLazyValue -> {
          result = declaration.buildIr(expression.startOffset, expression.endOffset) {
            resetInit(expression.extensionReceiver.cast()) ?: return result
          }
        }
        resetLazyValues -> {
          val vararg = expression.getValueArgument(0)?.castOrNull<IrVararg>() ?: return result

          result = declaration.buildIr(expression.startOffset, expression.endOffset) {
            irComposite {
              vararg.elements.forEach {
                resetInit(it.cast()).also { expr ->
                  if (expr == null) return result else +expr
                }
              }
            }
          }
        }
      }
      return result
    }

    private fun IrElement.transformCopiedDispatchReceiver(parent: IrFunction) = transformChildrenVoid(
      object : AbstractIrTransformer(pluginContext, configuration) {
        override fun visitGetValue(expression: IrGetValue): IrExpression = when {
          expression.isAccessToObject() -> IrGetValueImpl(startOffset, endOffset, parent.thisReceiver!!.symbol)
          else -> super.visitGetValue(expression)
        }
      }
    )
  }
}
