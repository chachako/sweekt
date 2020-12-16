package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi

/**
 * 声明 Kotlinpoet 的真实 [Spec]
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/12 - 00:28
 */
@DslApi
internal interface SpecDeclaration<Spec> {
  /** 返回真实的 Kotlinpoet-Spec */
  fun get(): Spec
}