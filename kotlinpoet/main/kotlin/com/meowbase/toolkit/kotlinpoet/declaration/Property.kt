package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass


typealias DeclareProperty = PropertyDeclaration.() -> Unit

/**
 * [ParameterSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class PropertyDeclaration(
  /** 这个属性的名称 */
  val name: String,

  @PublishedApi
  internal val builder: PropertySpec.Builder
) : SpecDeclaration<PropertySpec> {

  /**
   * 代表这个属性是类的主构造函数中的参数
   *
   * @see PropertySpec.Builder.isPrimaryConstructorParameter
   */
  fun primaryConstructor() = initializer(name)

  inline fun delegate(declare: DeclareCode) {
    builder.delegate(
      CodeDeclaration().also(declare).get()
    )
  }

  fun delegate(codeBlock: CodeBlock) {
    builder.delegate(codeBlock)
  }

  fun delegate(format: String, vararg args: Any?) {
    builder.delegate(format, args)
  }

  inline fun getter(declare: DeclareFun) {
    builder.getter(
      FunDeclaration("get()", FunSpec.getterBuilder()).also(declare).get()
    )
  }

  fun getter(format: String, vararg args: Any?) {
    getter { add(format, *args) }
  }

  inline fun setter(declare: DeclareFun) {
    builder.setter(
      FunDeclaration("set()", FunSpec.setterBuilder()).also(declare).get()
    )
  }

  fun setter(format: String, vararg args: Any?) {
    setter { add(format, *args) }
  }

  fun modifiers(vararg modifiers: KModifier) {
    builder.addModifiers(*modifiers)
  }

  fun modifiers(modifiers: Iterable<KModifier>) {
    builder.addModifiers(modifiers)
  }

  fun initializer(value: String) {
    builder.initializer(value)
  }

  inline fun initializer(declare: DeclareCode) {
    builder.initializer(
      CodeDeclaration().apply(declare).get()
    )
  }

  fun initializer(format: String, vararg args: Any?) {
    builder.initializer(format, *args)
  }

  fun kdoc(block: CodeBlock) {
    builder.addKdoc(block)
  }

  inline fun kdoc(declare: DeclareCode) {
    builder.addKdoc(
      CodeDeclaration().apply(declare).get()
    )
  }

  fun kdoc(format: String, vararg args: Any) {
    builder.addKdoc(format, *args)
  }

  operator fun KModifier.unaryPlus() = also { builder.addModifiers(it) }

  operator fun AnnotationSpec.unaryPlus() = also(builder::addAnnotation)

  override fun get(): PropertySpec = builder.build()
}


fun PropertySpec.toDeclaration(): PropertyDeclaration = PropertyDeclaration(name, this.toBuilder())


/**
 * 创建一个属性
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun buildProperty(
  name: String,
  type: TypeName,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = PropertyDeclaration(
  name,
  PropertySpec.builder(name, type, *modifiers).mutable(mutable)
).also(declare).get()

/**
 * 创建一个属性
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun buildProperty(
  name: String,
  type: KClass<*>,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = buildProperty(name, type.asTypeName(), mutable, *modifiers, declare = declare)

/**
 * 创建一个属性
 *
 * @param T 属性类型
 * @param name 属性名称
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun <reified T> buildProperty(
  name: String,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = buildProperty(name, T::class, mutable, *modifiers, declare = declare)


/**
 * 创建一个属性并添加到 [TypeDeclaration] 中
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun TypeDeclaration.property(
  name: String,
  type: TypeName,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = +PropertyDeclaration(
  name,
  PropertySpec.builder(name, type, *modifiers).mutable(mutable)
).also(declare).get()

/**
 * 创建一个属性并添加到 [TypeDeclaration] 中
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun TypeDeclaration.property(
  name: String,
  type: KClass<*>,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = property(name, type.asTypeName(), mutable, *modifiers, declare = declare)

/**
 * 创建一个属性并添加到 [TypeDeclaration] 中
 *
 * @param T 属性类型
 * @param name 属性名称
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun <reified T> TypeDeclaration.property(
  name: String,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = property(name, T::class, mutable, *modifiers, declare = declare)


/**
 * 创建一个属性并添加到 [FileDeclaration] 中
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun FileDeclaration.property(
  name: String,
  type: TypeName,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = +PropertyDeclaration(
  name,
  PropertySpec.builder(name, type, *modifiers).mutable(mutable)
).also(declare).get()

/**
 * 创建一个属性并添加到 [FileDeclaration] 中
 *
 * @param name 属性名称
 * @param type 属性类型
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun FileDeclaration.property(
  name: String,
  type: KClass<*>,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = property(name, type.asTypeName(), mutable, *modifiers, declare = declare)

/**
 * 创建一个属性并添加到 [FileDeclaration] 中
 *
 * @param T 属性类型
 * @param name 属性名称
 * @param modifiers 属性的修饰符
 * @param mutable true 为 var, 否则为 val
 * @param declare 对这个属性的其他声明
 */
inline fun <reified T> FileDeclaration.property(
  name: String,
  mutable: Boolean = false,
  vararg modifiers: KModifier,
  declare: DeclareProperty = {}
): PropertySpec = property(name, T::class, mutable, *modifiers, declare = declare)
