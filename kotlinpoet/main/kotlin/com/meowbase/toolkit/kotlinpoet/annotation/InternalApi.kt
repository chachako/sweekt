package com.meowbase.toolkit.kotlinpoet.annotation

/**
 * 标记一个类为内部 Api 的一部分
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/11/08 - 14:04
 */
@RequiresOptIn(
  "这是 'toolkit-kotlinpoet' 的一个内部 Api, 你不能从外部调用它。",
  level = RequiresOptIn.Level.ERROR
)
@Retention(value = AnnotationRetention.SOURCE)
internal annotation class InternalApi