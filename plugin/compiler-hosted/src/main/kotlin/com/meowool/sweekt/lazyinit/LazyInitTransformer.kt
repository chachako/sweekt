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
import com.meowool.sweekt.ModuleLoweringPass
import com.meowool.sweekt.SweektNames.LazyInit
import com.meowool.sweekt.SweektNames.resetLazyValue
import com.meowool.sweekt.SweektNames.resetLazyValues
import com.meowool.sweekt.SweektSyntheticDeclarationOrigin
import com.meowool.sweekt.cast
import com.meowool.sweekt.castOrNull
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBoolean
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irComposite
import org.jetbrains.kotlin.ir.builders.irElseBranch
import org.jetbrains.kotlin.ir.builders.irEquals
import org.jetbrains.kotlin.ir.builders.irFalse
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.builders.irTrue
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

/**
 * @author 凛 (RinOrz)
 */
class LazyInitTransformer(
  context: IrPluginContext,
  configuration: CompilerConfiguration,
) : AbstractIrTransformer(context, configuration), ModuleLoweringPass {
  private val propertiesOfIsInit = mutableMapOf<String, IrProperty>()

  override fun lower(module: IrModuleFragment) {
    module.transformChildrenVoid(this)
  }

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
    var property = super.visitPropertyNew(declaration) as IrProperty

    if (property.isFakeOverride || property.hasAnnotation(LazyInit).not()) return property

    // val -> var
    if (declaration.isVar.not() || property.backingField?.isFinal == true) {
      // The lazy init property does not need copy getter and setter
      property = property.copy { isVar = true }
      property.backingField = property.backingField?.transform(this, null).castOrNull()
    }

    val field = property.backingField ?: return property

    // var _isInit$name = false
    val isInitValue = property.getOrAddIsInitProperty(property.name.asString())

    // get() = when {
    //   _isInit$xxx && xxx == null -> field
    //   else -> ???.also {
    //     field = it
    //     _isInit$xxx = true
    //   }
    // }
    property.getOrAddGetter().also { getter ->
      getter.origin = SweektSyntheticDeclarationOrigin
      getter.body = getter.buildIr {
        irReturnExprBody(
          irWhen(property.type) {
            +irBranch(
              irEquals(irGetProperty(getter, isInitValue), irTrue()),
              irGetField(getter, property)
            )
            +irElseBranch(
              irBlock {
                val value = irTemporary(field.initializer!!.expression.transformGetOfThisClass(getter)).apply {
                  parent = getter
                }
                +irSetField(getter, property, irGet(value))
                +irSetProperty(getter, isInitValue, irTrue())
                +irGet(value)
              }
            )
          }
        )
      }
    }

    field.initializer = null
    return property
  }

  override fun visitFieldNew(declaration: IrField): IrStatement {
    val old = super.visitFieldNew(declaration) as IrField
    val isIncorrectFinal = declaration.isFinal && declaration.correspondingPropertySymbol?.owner?.isVar == true
    return when {
      isIncorrectFinal -> old.copy { isFinal = false }
      else -> old
    }
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

    when (expression.type.classFqName) {
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

  private fun IrExpression.transformGetOfThisClass(function: IrFunction) = transform(
    object : AbstractIrTransformer(pluginContext, configuration) {
      override fun visitGetValue(expression: IrGetValue): IrExpression = when {
        expression.isAccessThisClass() -> IrGetValueImpl(startOffset, endOffset, function.thisReceiver!!.symbol)
        else -> super.visitGetValue(expression)
      }
    },
    null
  )
}
