package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass


typealias DeclareLambdaTypeName = LambdaTypeDeclaration.() -> Unit

/**
 * [LambdaTypeName] 的 DSL 声明器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class LambdaTypeDeclaration(
  /**
   * 这个 Lambda 表达式的返回类型
   *
   * @see TypeName
   * @see KClass
   */
  var returns: Any
) : SpecDeclaration<LambdaTypeName> {
  private var parameterList: MutableList<ParameterSpec> = mutableListOf()

  /**
   * 这个 Lambda 表达式的接收器
   *
   * @see TypeName
   * @see KClass
   */
  var receiver: Any? = null

  /** 添加一个 [ParameterSpec] 到表达式参数列表中 */
  operator fun ParameterSpec.unaryPlus() = also { parameterList.add(it) }

  /** 添加一个 [ParameterList] 到表达式中 */
  operator fun ParameterList.unaryPlus() = also { parameterList.addAll(it) }


  private fun Any.checkTypeName(): TypeName = when (this) {
    is TypeName -> this
    is KClass<*> -> this.asTypeName()
    else -> error("类型只能是一个 TypeName 或 KClass<*>")
  }

  override fun get(): LambdaTypeName = LambdaTypeName.get(
    receiver?.checkTypeName(),
    parameterList,
    returns.checkTypeName()
  )
}


/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数列表
 */
fun TypeName.lambdaBy(
  vararg parameters: ParameterSpec,
  receiver: TypeName? = null
): LambdaTypeName =
  LambdaTypeName.get(receiver, *parameters, returnType = this)

/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数列表
 */
fun KClass<*>.lambdaBy(
  vararg parameters: ParameterSpec,
  receiver: TypeName? = null
): LambdaTypeName =
  LambdaTypeName.get(receiver, *parameters, returnType = this.asTypeName())


/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数类型列表
 */
fun TypeName.lambdaBy(vararg parameters: TypeName, receiver: TypeName? = null): LambdaTypeName =
  LambdaTypeName.get(receiver, *parameters, returnType = this)

/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数类型列表
 */
fun KClass<*>.lambdaBy(vararg parameters: TypeName, receiver: TypeName? = null): LambdaTypeName =
  LambdaTypeName.get(receiver, *parameters, returnType = this.asTypeName())


/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数列表
 */
fun TypeName.lambdaBy(parameters: ParameterList, receiver: TypeName? = null): LambdaTypeName =
  LambdaTypeName.get(receiver, parameters, returnType = this)

/**
 * 返回一个 [LambdaTypeName]
 * receiver.(parameterList) -> R
 *
 * @receiver lambda 的返回类型
 * @param receiver lambda 的接收器类型
 * @param parameters lambda 的参数列表
 */
fun KClass<*>.lambdaBy(parameters: ParameterList, receiver: TypeName? = null): LambdaTypeName =
  LambdaTypeName.get(receiver, parameters, returnType = this.asTypeName())


/**
 * 返回一个 [LambdaTypeName]
 *
 * @receiver lambda 的返回类型
 * @param declare 声明这个 Lambda 的更多信息
 */
fun TypeName.lambdaBy(declare: DeclareLambdaTypeName = {}): LambdaTypeName =
  LambdaTypeDeclaration(returns = this).apply(declare).get()

/**
 * 返回一个 [LambdaTypeName]
 *
 * @receiver lambda 的返回类型
 * @param declare 声明这个 Lambda 的更多信息
 */
fun KClass<*>.lambdaBy(declare: DeclareLambdaTypeName = {}): LambdaTypeName =
  LambdaTypeDeclaration(returns = this).apply(declare).get()
