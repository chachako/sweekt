@file:Suppress("OVERRIDE_BY_INLINE")

package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

typealias DeclareAnnotation = AnnotationDeclaration.() -> Unit
typealias AnnotationUseSiteTarget = AnnotationSpec.UseSiteTarget

/**
 * [TypeSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class AnnotationDeclaration(
  @PublishedApi
  internal val builder: AnnotationSpec.Builder
) : SpecDeclaration<AnnotationSpec>, Coding {

  @PublishedApi
  internal val body = CodeDeclaration()

  /**
   * 设置注解的使用处目标
   * https://kotlinlang.org/docs/reference/annotations.html#annotation-use-site-targets
   */
  var useSiteTarget: AnnotationUseSiteTarget? = null


  /**
   * 设置注解的成员
   *
   * @param declare 声明成员
   */
  inline fun member(declare: DeclareCode) {
    builder.addMember(
      CodeDeclaration().apply(declare).get()
    )
  }


  /**
   * 添加代码语句并作为注解的成员
   *
   * @param format 需要格式化的语句
   * @param args 格式化参数
   * @param line 如果为 true, 则代表这是一行语句，在尾端将换行
   */
  override fun add(format: String, vararg args: Any?, line: Boolean) =
    body.add(format, *args, line = line)

  /** 添加代码块 [codeBlock] */
  override fun add(codeBlock: CodeBlock) = body.add(codeBlock)

  /**
   * 添加一个缩进后的代码块并作为注解的成员
   *
   * @param coding 缩进的代码块
   */
  override fun indent(coding: DeclareCode) = body.indent(coding)

  /**
   * 创建一个流式 Api 并作为注解的成员
   *
   * @param controlFlow 控制流开始的语句
   * @param args 格式化 [controlFlow]
   * @param coding 这个控制流内的代码
   */
  override inline fun flow(controlFlow: String, vararg args: Any?, coding: DeclareCode) =
    body.flow(controlFlow, *args, coding = coding)

  /**
   * 创建调用语句
   *
   * @param target 调用的目标方法名称
   * @param declareParameters 声明调用的目标方法的参数
   */
  override inline fun call(target: String, crossinline declareParameters: DeclareCall) =
    body.call(target, declareParameters)


  override fun get(): AnnotationSpec = builder.addMember(body.get()).build()
}


fun AnnotationSpec.toDeclaration(): AnnotationDeclaration = AnnotationDeclaration(this.toBuilder())


/**
 * 创建一个注解
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun buildAnnotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun buildAnnotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun buildAnnotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> buildAnnotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = buildAnnotation(T::class, declare)


/**
 * 创建一个注解并添加到 [FunDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FunDeclaration.annotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FunDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FunDeclaration.annotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FunDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FunDeclaration.annotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FunDeclaration] 中
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> FunDeclaration.annotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = annotation(T::class, declare)


/**
 * 创建一个注解并添加到 [PropertyDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun PropertyDeclaration.annotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [PropertyDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun PropertyDeclaration.annotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [PropertyDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun PropertyDeclaration.annotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [PropertyDeclaration] 中
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> PropertyDeclaration.annotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = annotation(T::class, declare)


/**
 * 创建一个注解并添加到 [ParameterDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun ParameterDeclaration.annotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [ParameterDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun ParameterDeclaration.annotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [ParameterDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun ParameterDeclaration.annotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [ParameterDeclaration] 中
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> ParameterDeclaration.annotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = annotation(T::class, declare)


/**
 * 创建一个注解并添加到 [TypeDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun TypeDeclaration.annotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [TypeDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun TypeDeclaration.annotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [TypeDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun TypeDeclaration.annotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [TypeDeclaration] 中
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> TypeDeclaration.annotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = annotation(T::class, declare)


/**
 * 创建一个注解并添加到 [FileDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FileDeclaration.annotation(
  type: ClassName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FileDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FileDeclaration.annotation(
  type: ParameterizedTypeName,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FileDeclaration] 中
 *
 * @param type 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun FileDeclaration.annotation(
  type: KClass<out Annotation>,
  declare: DeclareAnnotation = {}
): AnnotationSpec = +AnnotationDeclaration(AnnotationSpec.builder(type)).also(declare).get()

/**
 * 创建一个注解并添加到 [FileDeclaration] 中
 *
 * @param T 注解类型
 * @param declare 对这个注解的其他声明
 */
inline fun <reified T: Annotation> FileDeclaration.annotation(
  declare: DeclareAnnotation = {}
): AnnotationSpec = annotation(T::class, declare)