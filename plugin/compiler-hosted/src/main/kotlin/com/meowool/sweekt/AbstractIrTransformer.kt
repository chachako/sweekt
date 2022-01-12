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
@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.meowool.sweekt

import com.meowool.sweekt.iteration.contains
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.ir.copyTo
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.IrGeneratorContext
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.builders.declarations.IrFieldBuilder
import org.jetbrains.kotlin.ir.builders.declarations.IrFunctionBuilder
import org.jetbrains.kotlin.ir.builders.declarations.IrPropertyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.declarations.buildProperty
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irExprBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetField
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irSet
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetField
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.expressions.getTypeArgument
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.isAnnotationClass
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.ir.util.isObject
import org.jetbrains.kotlin.ir.util.isPrimitiveArray
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.util.OperatorNameConventions

/**
 * @author 凛 (RinOrz)
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
abstract class AbstractIrTransformer(
  val pluginContext: IrPluginContext,
  val configuration: CompilerConfiguration
) : IrElementTransformerVoidWithContext() {
  val irFactory get() = pluginContext.irFactory
  val irBuiltIns get() = pluginContext.irBuiltIns

  val messenger = pluginContext.createDiagnosticReporter("Sweekt.${this.javaClass.simpleName}")

  inline fun log(logging: () -> Any) {
    if (configuration.get(SweektConfigurationKeys.isLogging, false))
      messenger.report(IrMessageLogger.Severity.INFO, logging().toString(), null)
  }

  fun IrElement.reportWarn(logging: Any, file: IrFile = currentFile) =
    messenger.report(IrMessageLogger.Severity.WARNING, logging.toString(), this.toMessageLocation(file))

  private fun IrElement.toMessageLocation(file: IrFile): IrMessageLogger.Location {
    val source = file.fileEntry.getSourceRangeInfo(startOffset, endOffset)
    return IrMessageLogger.Location(source.filePath, source.startLineNumber, source.startColumnNumber)
    //    return CompilerMessageLocationWithRange.create(
    //      path = source.filePath,
    //      lineStart = source.startLineNumber + 1,
    //      lineEnd = source.endLineNumber + 1,
    //      columnStart = source.startColumnNumber + 1,
    //      columnEnd = source.endColumnNumber + 1,
    //      lineContent = File(source.filePath).readLines()[source.startLineNumber]
    //    )
  }
//
//  fun log(element: IrElement, logging: Any, file: IrFile = currentIrFile) =
//    messenger.report(IrMessageLogger.Severity.INFO, logging.toString(), element.toMessageLocation(file))
//
//  fun error(element: IrElement, logging: Any, file: IrFile = currentIrFile) =
//    messenger.report(IrMessageLogger.Severity.ERROR, logging.toString(), element.toMessageLocation(file))

  // Compatible 1.5.31
  val intPlusSymbol: IrSimpleFunctionSymbol
    get() = irBuiltIns.intClass.functions.single {
      it.owner.name == OperatorNameConventions.PLUS && it.owner.valueParameters[0].type == irBuiltIns.intType
    }
  val intTimesSymbol: IrSimpleFunctionSymbol
    get() = irBuiltIns.intClass.functions.single {
      it.owner.name == OperatorNameConventions.TIMES && it.owner.valueParameters[0].type == irBuiltIns.intType
    }

  val IrTypeParameter.erasedUpperBound: IrClass
    get() {
      // Pick the (necessarily unique) non-interface upper bound if it exists
      for (type in superTypes) {
        val irClass = type.classOrNull?.owner ?: continue
        if (!irClass.isInterface && !irClass.isAnnotationClass) return irClass
      }

      // Otherwise, choose either the first IrClass supertype or recurse.
      // In the first case, all supertypes are interface types and the choice was arbitrary.
      // In the second case, there is only a single supertype.
      return when (val firstSuper = superTypes.first().classifierOrNull?.owner) {
        is IrClass -> firstSuper
        is IrTypeParameter -> firstSuper.erasedUpperBound
        else -> error("unknown supertype kind $firstSuper")
      }
    }

  class FunctionBodyBuilder(delegate: IrBuilderWithScope, val function: IrFunction) :
    IrBuilderWithScope(delegate.context, delegate.scope, delegate.startOffset, delegate.endOffset)

  class BranchBuilder(
    private val irWhen: IrWhen,
    context: IrGeneratorContext,
    scope: Scope,
    startOffset: Int,
    endOffset: Int
  ) : IrBuilderWithScope(context, scope, startOffset, endOffset) {
    operator fun IrBranch.unaryPlus() {
      irWhen.branches.add(this)
    }
  }

  fun IrGetValue.isAccessToObject(): Boolean {
    val owner = this.symbol.owner
    val expectedClass = this.type.classOrNull?.owner
    if (expectedClass == null || !expectedClass.isObject) return false
    return owner.origin == IrDeclarationOrigin.INSTANCE_RECEIVER || owner.name.asString() == "<this>"
  }

  fun IrBuilderWithScope.irWhen(
    type: IrType = context.irBuiltIns.unitType,
    block: BranchBuilder.() -> Unit
  ): IrWhen {
    val whenExpr = IrWhenImpl(startOffset, endOffset, type)
    val builder = BranchBuilder(whenExpr, context, scope, startOffset, endOffset)
    builder.block()
    return whenExpr
  }

  fun FunctionBodyBuilder.irThis() = irThisOrNull()!!
  fun FunctionBodyBuilder.irThisOrNull() = thisExpr(function)

  inline fun <R> IrSimpleFunction.buildIr(
    startOffset: Int = this.startOffset,
    endOffset: Int = this.endOffset,
    block: DeclarationIrBuilder.() -> R
  ): R = DeclarationIrBuilder(pluginContext, this.symbol, startOffset, endOffset).run(block)

  fun IrFunction.getValueParameter(name: Name) = valueParameters.singleOrNull { it.name == name }
    ?: error(valueParameters.map { it.name.asString() } + " does not contain $name")

  fun IrFunction.findValueParameter(name: Name) = valueParameters.singleOrNull { it.name == name }

  fun IrFunction.findValueParameter(name: String) = findValueParameter(Name.identifier(name))

  fun IrFunction.containsValueParameter(name: Name) = valueParameters.contains { it.name == name }

  fun Sequence<IrProperty>.getByName(name: Name) = single { it.name == name }

  inline fun <R> IrField.buildIr(
    startOffset: Int = this.startOffset,
    endOffset: Int = this.endOffset,
    block: DeclarationIrBuilder.() -> R
  ): R = DeclarationIrBuilder(pluginContext, this.symbol, startOffset, endOffset).run(block)

  fun IrBuilderWithScope.irCallWithArgs(
    callee: IrFunction,
    vararg valueArguments: IrExpression,
    dispatchReceiver: IrExpression? = null,
    extensionReceiver: IrExpression? = null,
    origin: IrStatementOrigin? = null,
    superQualifierSymbol: IrClassSymbol? = null
  ): IrCall {
    require(valueArguments.size == callee.valueParameters.size)
    return irCall(callee, origin, superQualifierSymbol).also {
      it.dispatchReceiver = dispatchReceiver
      it.extensionReceiver = extensionReceiver
      valueArguments.forEachIndexed { index, irExpression ->
        it.putValueArgument(index, irExpression)
      }
    }
  }

  fun IrBuilderWithScope.irCallWithArgs(
    callee: IrSimpleFunctionSymbol,
    vararg valueArguments: IrExpression,
    dispatchReceiver: IrExpression? = null,
    extensionReceiver: IrExpression? = null,
    origin: IrStatementOrigin? = null,
    superQualifierSymbol: IrClassSymbol? = null
  ): IrCall {
    require(valueArguments.size == callee.owner.valueParameters.size)
    return irCall(callee.owner, origin, superQualifierSymbol).also {
      it.dispatchReceiver = dispatchReceiver
      it.extensionReceiver = extensionReceiver
      valueArguments.forEachIndexed { index, irExpression ->
        it.putValueArgument(index, irExpression)
      }
    }
  }

  fun IrProperty.getOrAddGetter(): IrSimpleFunction = getter.ifNull {
    createGetter().also { getter = it }
  }

  inline fun IrProperty.createGetter(builder: IrFunctionBuilder.() -> Unit = {}): IrSimpleFunction =
    irFactory.buildFun {
      name = Name.special("<get-${this@createGetter.name}>")
      origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
      returnType = type
      getter?.let(::updateFrom)
      builder()
    }.also { getter ->
      getter.parent = this@createGetter.parent
      getter.correspondingPropertySymbol = this@createGetter.symbol
      getter.dispatchReceiverParameter = getter.thisReceiver
      if (backingField != null) {
        getter.body = getter.buildIr(UNDEFINED_OFFSET, UNDEFINED_OFFSET) {
          irReturnExprBody(irGetField(getter.dispatchReceiverParameter?.let(::irGet), this@createGetter))
        }
      }
    }

  inline fun IrProperty.createSetter(builder: IrFunctionBuilder.() -> Unit = {}): IrSimpleFunction =
    irFactory.buildFun {
      name = Name.special("<set-${this@createSetter.name}>")
      origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
      returnType = pluginContext.irBuiltIns.unitType
      setter?.let(::updateFrom)
      builder()
    }.also { setter ->
      setter.parent = this@createSetter.parent
      setter.correspondingPropertySymbol = this@createSetter.symbol
      setter.dispatchReceiverParameter = setter.thisReceiver
      val valueParam = setter.addValueParameter(Name.special("<set-?>"), type)
      if (backingField != null) {
        setter.body = setter.buildIr(UNDEFINED_OFFSET, UNDEFINED_OFFSET) {
          irExprBody(irSetField(setter.dispatchReceiverParameter?.let(::irGet), this@createSetter, irGet(valueParam)))
        }
      }
    }

  val IrProperty.isPrimaryConstructorParameter: Boolean
    get() = backingField?.initializer?.expression.castOrNull<IrGetValue>()?.origin == IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER

  val IrProperty.isClassBodyProperty: Boolean
    get() = isPrimaryConstructorParameter.not()

  val IrClass.superClass
    get() = superTypes.mapNotNull { it.classifierOrNull.castOrNull<IrClassSymbol>()?.owner }
      .filterNot { it.isInterface || it.isAnnotationClass }
      .singleOrNull()

  val IrDeclaration.parentContainer: IrDeclarationContainer get() = parent.cast()

  inline fun IrClass.findOverridenFunctionSymbols(predicate: (IrFunction) -> Boolean): List<IrSimpleFunctionSymbol> =
    findOverridenFunctions(predicate).map { it.symbol }

  inline fun IrClass.findOverridenFunctions(predicate: (IrFunction) -> Boolean): List<IrSimpleFunction> =
    superTypes.mapNotNull { it.getClass()?.functions?.singleOrNull(predicate) }

  inline fun IrDeclarationContainer.findFunctions(predicate: (IrFunction) -> Boolean): IrFunction? =
    declarations.find { it is IrFunction && predicate(it) } as? IrFunction

  fun IrBuilderWithScope.irSet(variable: IrVariable, value: IrExpression) =
    irSet(variable.symbol, value)

  fun IrBuilderWithScope.irReturnExprBody(value: IrExpression) =
    irExprBody(irReturn(value))

  fun IrBuilderWithScope.irGetField(receiver: IrExpression?, property: IrProperty): IrGetField =
    irGetField(receiver, property.backingField!!)

  fun IrBuilderWithScope.irGetField(scope: IrFunction, property: IrProperty): IrGetField =
    irGetField(thisExpr(scope), property)

  fun IrBuilderWithScope.irSetField(
    scope: IrFunction,
    property: IrProperty,
    value: IrExpression
  ): IrExpression = irSetField(thisExpr(scope), property.backingField!!, value)

  fun IrBuilderWithScope.irSetField(
    receiver: IrExpression?,
    property: IrProperty,
    value: IrExpression
  ): IrExpression = irSetField(receiver, property.backingField!!, value)

  fun IrBuilderWithScope.irGetProperty(receiver: IrExpression?, property: IrProperty): IrExpression {
    return if (property.getter != null)
      irGet(property.getter!!.returnType, receiver, property.getter!!.symbol)
    else
      irGetField(receiver, property.backingField!!)
  }

  fun IrBuilderWithScope.irSetProperty(
    receiver: IrExpression?,
    property: IrProperty,
    value: IrExpression
  ): IrExpression {
    return if (property.setter != null)
      irSet(property.setter!!.returnType, receiver, property.setter!!.symbol, value)
    else
      irSetField(receiver, property.backingField!!, value)
  }

  fun IrBuilderWithScope.irGetProperty(scope: IrFunction, property: IrProperty): IrExpression =
    irGetProperty(thisExpr(scope), property)

  fun IrBuilderWithScope.irSetProperty(
    scope: IrFunction,
    property: IrProperty,
    value: IrExpression
  ): IrExpression = irSetProperty(thisExpr(scope), property, value)

  fun IrDeclarationParent.addProperty(
    name: String,
    original: IrProperty? = null,
    type: IrType? = original?.type,
    isStatic: Boolean = original?.run { descriptor.dispatchReceiverParameter == null } ?: false,
    initializer: (IrBuilderWithScope.() -> IrExpression)? = null,
    builder: IrPropertyBuilder.() -> Unit = {}
  ): IrProperty {
    val parent = this
    return irFactory.buildProperty {
      if (original != null) updateFrom(original)
      builder()
      this.name = Name.identifier(name)
      isVar = initializer != null
      origin = SweektSyntheticDeclarationOrigin
    }.also { property ->
      property.parent = parent
      property.backingField = irFactory.buildField {
        this.name = property.name
        this.type = type ?: pluginContext.irBuiltIns.anyType
        this.isStatic = isStatic
      }.also { field ->
        field.parent = parent
        field.correspondingPropertySymbol = property.symbol
        if (initializer != null)
          field.initializer = field.buildIr(field.startOffset, field.endOffset) { irExprBody(initializer()) }
      }
      parent.cast<IrDeclarationContainer>().declarations.add(property)
    }
  }

  fun IrDeclarationParent.addFunction(
    original: IrFunction? = null,
    builder: IrFunctionBuilder.() -> Unit
  ) = irFactory.buildFun {
    original?.let(::updateFrom)
    origin = SweektSyntheticDeclarationOrigin
    builder()
  }.also { function ->
    function.parent = this
    function.dispatchReceiverParameter = this.castOrNull<IrClass>()?.thisReceiver?.copyTo(function)
    this.cast<IrDeclarationContainer>().declarations.add(function)
  }

  val IrProperty.type: IrType
    get() = backingField?.type ?: getter?.returnType ?: descriptor.type.let(pluginContext.typeTranslator::translateType)

  val IrFunction.thisReceiver: IrValueParameter?
    get() = dispatchReceiverParameter.ifNull { parent.castOrNull<IrClass>()?.thisReceiver?.copyTo(this) }

  fun IrBuilderWithScope.thisExpr(function: IrFunction) = function.thisReceiver?.let(::irGet)

  fun IrBuilderWithScope.irAndAnd(vararg args: IrExpression): IrExpression {
    if (args.isEmpty()) throw AssertionError("At least one argument expected")
    var result = args[0]
    for (i in 1 until args.size) {
      result = irCall(context.irBuiltIns.andandSymbol).apply {
        putValueArgument(0, result)
        putValueArgument(1, args[i])
      }
    }
    return result
  }

  fun IrType.isBoxedOrPrimitiveArray() = this.isArray() || this.isPrimitiveArray()

  fun IrGetValue.isAccessThisClass(): Boolean {
    val target = this.symbol.owner as? IrValueParameter ?: return false
    val expectedClass = this.type.classOrNull?.owner
    if (expectedClass == null || expectedClass.isObject) return false
    return target.origin == IrDeclarationOrigin.INSTANCE_RECEIVER || target.name.asString() == "<this>"
  }

  fun IrField.copy(builder: IrFieldBuilder.() -> Unit = {}) = let { old ->
    irFactory.buildField {
      name = old.name
      updateFrom(old)
      builder()
    }.apply {
      parent = old.parent
      annotations = old.annotations
      metadata = old.metadata
      correspondingPropertySymbol = old.correspondingPropertySymbol
      initializer = old.initializer
    }
  }

  fun IrProperty.copy(builder: IrPropertyBuilder.() -> Unit = {}) = let { old ->
    irFactory.buildProperty {
      name = old.name
      modality = old.modality
      updateFrom(old)
      builder()
    }.apply {
      parent = old.parent
      annotations = old.annotations
      metadata = old.metadata
      attributeOwnerId = old.attributeOwnerId
      overriddenSymbols = old.overriddenSymbols
      getter = old.getter?.also { it.correspondingPropertySymbol = symbol }
      setter = old.setter?.also { it.correspondingPropertySymbol = symbol }
      backingField = old.backingField?.also { it.correspondingPropertySymbol = symbol }
    }
  }

  fun IrFunction.copy(builder: IrFunctionBuilder.() -> Unit = {}) = let { old ->
    this.castOrNull<IrSimpleFunction>()?.copy(builder) ?: irFactory.buildFun {
      name = old.name
      originalDeclaration = old
      updateFrom(old)
      builder()
    }.apply {
      parent = old.parent
      annotations = old.annotations
      metadata = old.metadata
      returnType = old.returnType
      dispatchReceiverParameter = old.dispatchReceiverParameter
      extensionReceiverParameter = old.extensionReceiverParameter
      valueParameters = old.valueParameters
      body = old.body
    }
  }

  fun IrSimpleFunction.copy(builder: IrFunctionBuilder.() -> Unit = {}) = let { old ->
    irFactory.buildFun {
      name = old.name
      originalDeclaration = old
      updateFrom(old)
      builder()
    }.apply {
      parent = old.parent
      annotations = old.annotations
      metadata = old.metadata
      attributeOwnerId = old.attributeOwnerId
      overriddenSymbols = old.overriddenSymbols
      correspondingPropertySymbol = old.correspondingPropertySymbol
      returnType = old.returnType
      dispatchReceiverParameter = old.dispatchReceiverParameter
      extensionReceiverParameter = old.extensionReceiverParameter
      valueParameters = old.valueParameters
      body = old.body
    }
  }

  fun IrCall.copy(
    startOffset: Int = this.startOffset,
    endOffset: Int = this.endOffset,
    type: IrType = this.type,
    symbol: IrSimpleFunctionSymbol = this.symbol,
    typeArgumentsCount: Int = this.typeArgumentsCount,
    valueArgumentsCount: Int = this.valueArgumentsCount,
    origin: IrStatementOrigin? = this.origin,
    superQualifierSymbol: IrClassSymbol? = this.superQualifierSymbol,
  ) = let { old ->
    IrCallImpl(
      startOffset,
      endOffset,
      type,
      symbol,
      typeArgumentsCount,
      valueArgumentsCount,
      origin,
      superQualifierSymbol
    ).apply {
      attributeOwnerId = old.attributeOwnerId
      dispatchReceiver = old.dispatchReceiver
      extensionReceiver = old.extensionReceiver
      for (i in 0 until old.valueArgumentsCount) {
        putValueArgument(i, old.getValueArgument(i))
      }
      for (i in 0 until old.typeArgumentsCount) {
        putTypeArgument(i, old.getTypeArgument(i))
      }
    }
  }
}

fun IrConstructorCall.getAnnotationBooleanOrNull(name: String): Boolean? {
  val parameter = symbol.owner.valueParameters.single { it.name.asString() == name }
  return getValueArgument(parameter.index).castOrNull<IrConst<Boolean>>()?.value
}
