package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*

typealias DeclareType = TypeDeclaration.() -> Unit

/**
 * [TypeSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class TypeDeclaration(
  /**
   * 这个类型的名称
   * 如果这是一个匿名类，则为 null
   */
  val name: String?,

  @PublishedApi
  internal val builder: TypeSpec.Builder
) : SpecDeclaration<TypeSpec> {

  /** 添加一个 [FunSpec] 到 Type 中 */
  fun constructor(primary: Boolean = false, declare: DeclareFun = {}) {
    FunDeclaration("constructor()", FunSpec.constructorBuilder()).also(declare).get().also {
      when {
        primary -> builder.primaryConstructor(it)
        else -> +it
      }
    }
  }

  /** 添加 Type 的修饰符 */
  fun modifiers(vararg modifiers: KModifier) {
    builder.addModifiers(*modifiers)
  }

  /** 添加 Type 的修饰符 */
  fun modifiers(modifiers: Iterable<KModifier>) = apply {
    builder.addModifiers(modifiers)
  }

  /**
   * 设置 Type 的 Kotlin 文档
   *
   * @param block 代码块
   */
  fun kdoc(block: CodeBlock) {
    builder.addKdoc(block)
  }

  /**
   * 设置 Type 的 Kotlin 文档
   *
   * @param declare 声明 kdoc
   */
  inline fun kdoc(declare: DeclareCode) {
    builder.addKdoc(
      CodeDeclaration().apply(declare).get()
    )
  }

  /**
   * 设置 Type 的 Kotlin 文档
   *
   * @param format 需要格式化的语句
   * @param args 格式化参数
   */
  fun kdoc(format: String, vararg args: Any) {
    builder.addKdoc(format, *args)
  }

  /** 添加一个 [FunSpec] 到这个类型中 */
  operator fun FunSpec.unaryPlus() = also(builder::addFunction)

  /** 添加一个 [PropertySpec] 到这个类型中 */
  operator fun PropertySpec.unaryPlus() = also(builder::addProperty)

  /** 添加一个 [AnnotationSpec] 到这个类型中 */
  operator fun AnnotationSpec.unaryPlus() = also(builder::addAnnotation)

  override fun get(): TypeSpec = builder.build()
}


fun TypeSpec.toDeclaration(): TypeDeclaration = TypeDeclaration(name, this.toBuilder())

fun TypeSpec.asClassName(packageName: String): ClassName =
  ClassName(packageName, name ?: error("匿名类无法作为 ClassName."))