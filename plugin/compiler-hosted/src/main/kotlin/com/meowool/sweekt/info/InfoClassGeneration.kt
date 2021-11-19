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

 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("NAME_SHADOWING", "RemoveRedundantSpreadOperator")

package com.meowool.sweekt.info

import com.meowool.sweekt.AbstractIrTransformer
import com.meowool.sweekt.SweektNames.ContentEquals
import com.meowool.sweekt.SweektNames.FullInfoSynthetic
import com.meowool.sweekt.SweektNames.Info
import com.meowool.sweekt.SweektNames.InfoInvisible
import com.meowool.sweekt.SweektSyntheticDeclarationOrigin
import com.meowool.sweekt.ifNull
import com.meowool.sweekt.info.InfoClassDescriptorResolver.CopyFunctionName
import com.meowool.sweekt.let
import org.jetbrains.kotlin.backend.common.ClassLoweringPass
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower
import org.jetbrains.kotlin.backend.common.lower.irNot
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.IrFunctionBuilder
import org.jetbrains.kotlin.ir.builders.irAs
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallOp
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irEqeqeq
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irIfNull
import org.jetbrains.kotlin.ir.builders.irIfThenReturnFalse
import org.jetbrains.kotlin.ir.builders.irIfThenReturnTrue
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irNotEquals
import org.jetbrains.kotlin.ir.builders.irNotIs
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irReturnTrue
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.overrides.isOverridableMemberOrAccessor
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.findDeclaration
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isPrimitiveArray
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

/**
 * @author 凛 (https://github.com/RinOrz)
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
class InfoClassGeneration(val configuration: CompilerConfiguration) : IrGenerationExtension {
  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) =
    Transformer(pluginContext).lower(moduleFragment)

  private inner class Transformer(context: IrPluginContext) :
    AbstractIrTransformer(context, configuration),
    ClassLoweringPass {

    override fun lower(irClass: IrClass) {
      val info = irClass.getAnnotation(Info)?.let(::Info) ?: return
      val properties = irClass.collectProperties(info)
      val functions = irClass.functions
      val constructorEntry = irClass.primaryConstructor ?: irClass.constructors.single()

      // override fun copy(...): ?
      if (info.generateCopy) collectCopyParameters(constructorEntry, properties).also { copyParameters ->
        CopyGeneration(irClass, constructorEntry, copyParameters)
          .generateIfUndefine(functions, properties) { isCopy(copyParameters) }
      }

      // override fun equals(other: Any?): Boolean
      if (info.generateEquals) EqualsGeneration(irClass, info.callSuperEquals)
        .generateIfUndefine(functions, properties, allowEmptyProperties = false) { isEquals() }

      // override fun hashCode(): Int
      if (info.generateHashCode) HashCodeGeneration(irClass, info.callSuperHashCode)
        .generateIfUndefine(functions, properties, allowEmptyProperties = false) { isHashCode() }

      // override fun toString(): String
      if (info.generateToString) ToStringGeneration(irClass)
        .generateIfUndefine(functions, properties) { isToString() }

      // operator fun componentN(): ?
      if (info.generateComponentN) properties.toList().filter {
        val infoInvisible = it.getAnnotation(InfoInvisible)?.let(::InfoInvisible)
        infoInvisible == null || infoInvisible.generateComponentN
      }.also {
        log { "generateComponentN for [${it.joinToString { it.name.asString() }}]" }
      }.forEachIndexed { index, property ->
        val n = index + 1
        ComponentNGeneration(irClass, n, property)
          .generateIfUndefine(functions, emptySequence()) { isComponentN(n) }
      }
    }

    private fun FunctionGeneration.generateIfUndefine(
      functions: Sequence<IrSimpleFunction>,
      properties: Sequence<IrProperty>,
      allowEmptyProperties: Boolean = true,
      isSameSignature: IrSimpleFunction.() -> Boolean
    ) {
      when (val function = functions.singleOrNull { it.isSameSignature() }) {
        null -> generate(properties, allowEmptyProperties)
        // If the function with the same signature is manually defined, it will not be generated
        else -> if (function.isFakeOverride || function.body == null) {
          irClass.declarations.remove(function)
          generate(properties, allowEmptyProperties)
        } else {
          generate(properties, allowEmptyProperties, onlyProxy = true)
        }
      }
    }

    private fun IrClass.collectProperties(info: Info) = properties.filter {
      if (info.joinPrivateProperties.not() && DescriptorVisibilities.isPrivate(it.visibility)) return@filter false
      if (info.joinPrimaryProperties.not() && it.isPrimaryConstructorParameter) return@filter false
      if (info.joinBodyProperties.not() && it.isClassBodyProperty) return@filter false
      it.origin == IrDeclarationOrigin.DEFINED
    }

    private fun collectCopyParameters(
      constructor: IrConstructor,
      properties: Sequence<IrProperty>
    ): List<FunctionParameterId> {
      // For example:
      //   class Foo(required: Int, private val isInit: Boolean) {
      //     lateinit var name: String
      //
      // The 'copy' function signature:
      //   fun copy(
      //     required: Int,
      //     isInit: Boolean = this.isInit,
      //     name: String = this.name
      //   ): Foo
      //
      // =================================================================================
      // "Non-property" parameters in the constructor are treated as required parameters
      val requiredParams = constructor.valueParameters.filter { param ->
        properties.none { param.name == it.name }
      }.map { FunctionParameterId(it.name, it.type) }

      val bodyProperties = properties.filter {
        val infoInvisible = it.getAnnotation(InfoInvisible)?.let(::InfoInvisible)
        infoInvisible == null || infoInvisible.generateCopy
      }.map { FunctionParameterId(it.name, it.type) }

      return requiredParams + bodyProperties
    }

    private fun IrFunction.isCopy(exceptedParameters: List<FunctionParameterId>): Boolean = name == CopyFunctionName &&
      returnType == parentAsClass.defaultType &&
      extensionReceiverParameter == null && typeParameters.isEmpty() &&
      valueParameters.map { it.type } == exceptedParameters.map { it.type }

    private fun IrFunction.isHashCode(): Boolean = name.asString() == "hashCode" && returnType == irBuiltIns.intType &&
      extensionReceiverParameter == null && valueParameters.isEmpty() && typeParameters.isEmpty()

    private fun IrFunction.isEquals(): Boolean = name.asString() == "equals" && returnType == irBuiltIns.booleanType &&
      extensionReceiverParameter == null && typeParameters.isEmpty() &&
      valueParameters.singleOrNull()?.type == irBuiltIns.anyNType

    private fun IrFunction.isToString(): Boolean = name.asString() == "toString" && returnType == irBuiltIns.stringType &&
      extensionReceiverParameter == null && typeParameters.isEmpty() && valueParameters.isEmpty()

    private fun IrFunction.isComponentN(number: Int): Boolean = name.asString() == "component$number" &&
      extensionReceiverParameter == null && typeParameters.isEmpty() && valueParameters.isEmpty()

    private abstract inner class FunctionGeneration(
      val irClass: IrClass,
      val functionName: String,
      val functionProxyName: String? = null,
      private val returnType: IrType,
      private vararg val parameters: FunctionParameterId,
    ) {
      constructor(
        irClass: IrClass,
        functionName: String,
        functionProxyName: String,
        returnType: IrType,
        vararg parameters: IrType,
      ) : this(
        irClass,
        functionName,
        functionProxyName,
        returnType,
        *parameters.mapIndexed { index, type ->
          FunctionParameterId(Name.identifier("arg$index"), type)
        }.toTypedArray(),
      )

      constructor(
        irClass: IrClass,
        functionName: String,
        functionProxyName: String,
        returnType: IrType,
      ) : this(
        irClass,
        functionName,
        functionProxyName,
        returnType,
        *emptyArray<IrType>(),
      )

      val irType = irClass.defaultType

      fun generate(properties: Sequence<IrProperty>, allowEmptyProperties: Boolean = true, onlyProxy: Boolean = false) {
        val properties = properties.filter {
          val infoInvisible = it.getAnnotation(InfoInvisible)?.let(::InfoInvisible)
          infoInvisible == null || isJoin(infoInvisible)
        }.toMutableList()

        val proxy = generateProxy(properties)

        if (allowEmptyProperties.not() && properties.isEmpty()) return
        if (onlyProxy) return

        addFunction(functionName) {
          isOperator = isOperator()
          visibility = DescriptorVisibilities.PUBLIC
        }.also { func ->
          func.overriddenSymbols = irClass.findOverridenFunctionSymbols { it.isSameSignature() }
          func.body = func.buildIr {
            when (proxy) {
              null -> FunctionBodyBuilder(this, func).body(properties)
              else -> irReturnExprBody(
                irCall(proxy).also { call ->
                  call.dispatchReceiver = thisExpr(func)
                  func.valueParameters.forEachIndexed { index, param ->
                    call.putValueArgument(index, irGet(param))
                  }
                }
              )
            }
          }
        }
      }

      fun generateProxy(properties: MutableList<IrProperty>): IrSimpleFunction? {
        fun IrFunction.isProxyFunction(exceptedName: String) =
          name.asString() == exceptedName && valueParameters.map { it.type } == parameters.map { it.type }

        val proxyName = functionProxyName ?: return null

        irClass.findDeclaration<IrFunction> { func -> func.isProxyFunction(proxyName) && func.isFakeOverride }?.also {
          irClass.declarations -= it
        }

        return addFunction(proxyName).also { func ->
          func.overriddenSymbols = irClass.findOverridenFunctionSymbols {
            func.isProxyFunction(proxyName) && it.isOverridableMemberOrAccessor() &&
              it.parentClassOrNull?.kotlinFqName == FullInfoSynthetic
          }
          func.body = func.buildIr { FunctionBodyBuilder(this, func).body(properties) }
        }
      }

      private fun addFunction(
        nameIdentifier: String,
        builder: IrFunctionBuilder.() -> Unit = {},
      ) = irClass.addFunction {
        name = Name.identifier(nameIdentifier)
        returnType = this@FunctionGeneration.returnType
        startOffset = SYNTHETIC_OFFSET
        endOffset = SYNTHETIC_OFFSET
        visibility = DescriptorVisibilities.PRIVATE
        builder(this)
      }.also { func ->
        func.valueParameters = parameters.mapIndexed { index, id ->
          irFactory.createValueParameter(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            SweektSyntheticDeclarationOrigin,
            IrValueParameterSymbolImpl(),
            id.name,
            index,
            id.type,
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false,
          ).apply { parent = func }
        }
      }

      protected open fun isOperator(): Boolean = false

      protected abstract fun IrFunction.isSameSignature(): Boolean

      protected abstract fun isJoin(infoInvisible: InfoInvisible): Boolean

      protected abstract fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody

      fun IrClass.findSuperFunction(
        containsAny: Boolean = true,
        requireOrigin: IrDeclarationOrigin? = null
      ): IrFunction? {
        var result: IrFunction? = null
        var superClass = this.superClass
        while (superClass != null && result == null) {
          result = superClass.findFunctions {
            if (requireOrigin != null && it.origin != requireOrigin) return@findFunctions false
            it.isSameSignature()
          }
          superClass = superClass.superClass
        }
        return when {
          containsAny -> result.ifNull { irBuiltIns.anyClass.getSimpleFunction(functionName)!!.owner }
          else -> result?.takeIf { it.parentAsClass.symbol != irBuiltIns.anyClass }
        }
      }

      fun IrClass.getOrSuperFunction(): IrFunction = findFunctions { it.isHashCode() } ?: findSuperFunction()!!

      fun FunctionBodyBuilder.callSuperFunctionIfDefined(
        vararg arguments: IrExpression,
        containsAny: Boolean = false
      ): IrCall? =
        irClass.findSuperFunction(containsAny, requireOrigin = IrDeclarationOrigin.DEFINED)?.let {
          irCallWithArgs(
            it, *arguments,
            dispatchReceiver = irThisOrNull(),
            superQualifierSymbol = it.parentAsClass.symbol
          )
        }
    }

    /**
     * fun copy(a: Int, b: Int, c: Int): Foo {
     *   val result = Foo(a)
     *   result.b = b
     *   result.c = c
     * }
     *
     * @author 凛 (https://github.com/RinOrz)
     */
    private inner class CopyGeneration(
      irClass: IrClass,
      private val constructorEntry: IrConstructor,
      private val copyParameters: List<FunctionParameterId>,
    ) : FunctionGeneration(
      irClass,
      functionName = "copy",
      returnType = irClass.defaultType,
      parameters = copyParameters.toTypedArray()
    ) {
      override fun IrFunction.isSameSignature(): Boolean = isCopy(copyParameters)

      override fun isJoin(infoInvisible: InfoInvisible): Boolean = infoInvisible.generateCopy

      override fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody = irBlockBody {
        log { "generateCopy for [${function.valueParameters.joinToString { it.name.asString() }}]" }

        // Initialize default values of copy-function parameters
        // -- fun copy(a = this.a, b = this.b, c = this.c)
        function.valueParameters.forEach { parameter ->
          properties.singleOrNull { it.name == parameter.name }?.apply {
            irGetProperty(irThis(), this)
          }
        }

        val instance = irCall(constructorEntry.symbol).also { call ->
          // Call target constructor to create new instance
          constructorEntry.valueParameters.forEach {
            // fun copy(a, b, c)
            //   Foo(a, b, c)
            val getParamOrProperty = function.findValueParameter(it.name)?.let(::irGet)
              ?: irGetProperty(irThis(), irClass.properties.getByName(it.name))
            call.putValueArgument(it.index, getParamOrProperty)
          }
        }
        // val result = Foo(a)
        val result = irTemporary(instance, "result")

        // Set all 'var' properties that are not in the primary constructor
        properties.filterNot { !it.isVar || constructorEntry.containsValueParameter(it.name) }.forEach {
          // result.a = a
          +irSetProperty(irGet(result), it, irGet(function.getValueParameter(it.name)))
        }

        +irReturn(irGet(result))
      }

      fun getRequiredParams() = irClass.primaryConstructor ?: irClass.constructors.single()
    }

    private inner class FunctionParameterId(val name: Name, val type: IrType)

    /**
     * override fun equals(other: Any?): Boolean {
     *   if (!super.equals(other)) return false
     *
     *   if (this === other) return true
     *   if (other !is Foo) return false
     *
     *   if (a != other.a) return false
     *   if (!b.contentEquals(other.b) return false
     *
     *   return true
     * }
     *
     * @author 凛 (https://github.com/RinOrz)
     */
    private inner class EqualsGeneration(irClass: IrClass, private val superCall: Boolean) :
      FunctionGeneration(irClass, "equals", "infoEquals", irBuiltIns.booleanType, irBuiltIns.anyNType) {

      override fun IrFunction.isSameSignature(): Boolean = isEquals()

      override fun isJoin(infoInvisible: InfoInvisible): Boolean = infoInvisible.generateEquals

      override fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody = irBlockBody {
        log { "generateEquals for [${properties.joinToString { it.name.asString() }}]" }

        if (properties.isEmpty()) return@irBlockBody +irReturn(
          callSuperFunctionIfDefined(irOther(), containsAny = true)!!
        )

        irGet(function.valueParameters.single())
        // if (!super.equals(other)) return false
        if (superCall) callSuperFunctionIfDefined(irOther())?.also {
          +irIfThenReturnFalse(irNot(it))
        }
        // if (this === other) return true
        if (!irClass.isInline) +irIfThenReturnTrue(irEqeqeq(irThis(), irOther()))
        // if (other !is Foo) return false
        +irIfThenReturnFalse(irNotIs(irOther(), irType))
        // other as Foo
        val castOther = irTemporary(irAs(irOther(), irType), "castOther").symbol
        for (property in properties) {
          val type = property.type
          val baseValue = irGetProperty(irThis(), property)
          val otherValue = irGetProperty(irGet(irType, castOther), property)
          when {
            // if (!a.contentEquals(other.a)) return false
            type.isBoxedOrPrimitiveArray() -> +irIfThenReturnFalse(
              irNot(irCallWithArgs(getArrayContentEquals(type), baseValue, extensionReceiver = otherValue))
            )
            // if (a != other.a) return false
            else -> +irIfThenReturnFalse(
              irNotEquals(baseValue, otherValue)
            )
          }
        }
        +irReturnTrue()
      }

      private fun FunctionBodyBuilder.irOther() = irGet(function.valueParameters.single())

      private val arrayContentEquals by lazy { pluginContext.referenceFunctions(ContentEquals).map { it.owner } }

      private fun getArrayContentEquals(type: IrType) = when {
        type.isPrimitiveArray() -> arrayContentEquals.single { it.extensionReceiverParameter?.type == type }
        else -> arrayContentEquals.singleOrNull { it.extensionReceiverParameter?.type?.isArray() == true }
      } ?: error("Can't find an Arrays.contentEquals method for array type ${type.render()}")
    }

    /**
     * override fun hashCode(): Int {
     *   var result = super.hashCode()
     *   result = 31 * result + (a?.hashCode() ?: 0)
     *   result = 31 * result + b.hashCode()
     *   result = 31 * result + c.contentHashCode()
     *   return result
     * }
     *
     * @author 凛 (https://github.com/RinOrz)
     */
    private inner class HashCodeGeneration(irClass: IrClass, private val superCall: Boolean) :
      FunctionGeneration(irClass, "hashCode", "infoHashCode", irBuiltIns.intType) {

      override fun IrFunction.isSameSignature(): Boolean = isHashCode()

      override fun isJoin(infoInvisible: InfoInvisible): Boolean = infoInvisible.generateHashCode

      override fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody = irBlockBody {
        log { "generateHashCode for [${properties.joinToString { it.name.asString() }}]" }

        if (properties.isEmpty()) return@irBlockBody +irReturn(callSuperFunctionIfDefined(containsAny = true)!!)

        // -- If the parent types defines 'hashCode', call the super function:
        //    var result = super.hashCode()
        // -- Otherwise:
        //    var result = a.hashCode()
        val hashCode = when {
          superCall -> callSuperFunctionIfDefined()?.also { log { "super call '${it.symbol.descriptor.fqNameSafe}'." } }
          else -> null
        }.ifNull { callPropertyHashCode(function, properties.removeFirst(), default = 1) }
        val result = irTemporary(hashCode, "result", isMutable = true)

        properties.forEach {
          // result = 31 * result + (b?.hashCode() ?: 0)
          // result = 31 * result + c.hashCode()
          val shiftedResult = shiftHashCode(result)
          val propertyHashCode = callPropertyHashCode(function, it)
          +irSet(result, plusHashCode(shiftedResult, propertyHashCode))
        }
        +irReturn(irGet(result))
      }

      /** '31 * result' */
      private fun IrBuilderWithScope.shiftHashCode(result: IrVariable): IrExpression =
        irCallOp(irBuiltIns.intTimesSymbol, irBuiltIns.intType, irInt(31), irGet(result))

      /** shiftedResult + propertyHashCode  */
      private fun IrBuilderWithScope.plusHashCode(
        shiftedResult: IrExpression,
        propertyHashCode: IrExpression
      ): IrExpression = irCallOp(irBuiltIns.intPlusSymbol, irBuiltIns.intType, shiftedResult, propertyHashCode)

      private fun IrBuilderWithScope.callPropertyHashCode(
        dispatch: IrFunction,
        property: IrProperty,
        default: Int = 0
      ): IrExpression {
        val hashCodeFunction = property.type.getHashCodeFunction()
        val hasDispatchReceiver = hashCodeFunction.descriptor.dispatchReceiverParameter != null
        val propertyValue = irGetProperty(dispatch, property)
        val hashCodeCall = when {
          hasDispatchReceiver -> irCallWithArgs(hashCodeFunction, dispatchReceiver = propertyValue)
          hashCodeFunction.valueParameters.size == 1 -> irCallWithArgs(hashCodeFunction, propertyValue)
          else -> error("Cannot call '${hashCodeFunction.render()}' for '$property'.")
        }
        // if (property == null) 'default' else property.hashCode()
        return when {
          property.type.isNullable() -> irIfNull(
            type = irBuiltIns.intType,
            subject = irGetProperty(dispatch, property),
            thenPart = irInt(default),
            elsePart = hashCodeCall
          )
          else -> hashCodeCall
        }
      }

      private fun IrType.getHashCodeFunction(): IrFunction {
        val classifier = classifierOrNull
        return when {
          isBoxedOrPrimitiveArray() -> irBuiltIns.dataClassArrayMemberHashCodeSymbol.owner
          classifier is IrClassSymbol -> classifier.owner.getOrSuperFunction()
          classifier is IrTypeParameterSymbol -> classifier.owner.erasedUpperBound.getOrSuperFunction()
          else -> error("Unknown classifier kind $classifier")
        }
      }
    }

    /**
     * override fun toString(): String =
     *   "User(name=$name, address=${address.contentToString()}, age=$age, email=$email)"
     *
     * @author 凛 (https://github.com/RinOrz)
     */
    private inner class ToStringGeneration(irClass: IrClass) :
      FunctionGeneration(irClass, "toString", "infoToString", irBuiltIns.stringType) {

      override fun IrFunction.isSameSignature(): Boolean = isToString()

      override fun isJoin(infoInvisible: InfoInvisible): Boolean = infoInvisible.generateToString

      override fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody = irReturnExprBody(
        irConcat().apply {
          log { "generateToString for [${properties.joinToString { it.name.asString() }}]" }

          // User(
          arguments += irString(irClass.name.asString() + "(")
          properties.forEachIndexed { index, property ->
            val value = irGetProperty(irThis(), property)
            val type = property.type

            // prev, next
            if (index > 0) arguments += irString(", ")

            // foo=
            arguments += irString(property.name.asString() + "=")
            arguments += when {
              // ${bar.contentToString()}
              type.isBoxedOrPrimitiveArray() -> irCallWithArgs(irBuiltIns.dataClassArrayMemberToStringSymbol, value)
              // $bar
              else -> value
            }
          }
          // )
          arguments += irString(")")
        }
      )
    }

    /**
     * operator fun component1(): String = a
     * operator fun component2(): String = b
     *
     * @author 凛 (https://github.com/RinOrz)
     */
    private inner class ComponentNGeneration(
      irClass: IrClass,
      private val number: Int,
      private val property: IrProperty
    ) : FunctionGeneration(irClass, "component$number", null, property.type) {

      override fun isOperator(): Boolean = true

      override fun IrFunction.isSameSignature(): Boolean = isComponentN(number)

      override fun isJoin(infoInvisible: InfoInvisible): Boolean = true

      override fun FunctionBodyBuilder.body(properties: MutableList<IrProperty>): IrBody =
        irReturnExprBody(irGetProperty(irThis(), property))
    }
  }
}
