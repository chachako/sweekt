package com.meowbase.toolkit.kotlinpoet.utility


/**
 * KotlinPoet-ClassName 相关的 Api 优化
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 21:01
 */
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName


/** 简单的将字符串转换为 [MemberName] */
fun String.asMemberName() = MemberName(
  this.substringBeforeLast("."),
  this.substringAfterLast(".")
)


/** 简单的将字符串转换为 [ClassName] */
fun String.asClassName() = ClassName(
  this.substringBeforeLast("."),
  this.substringAfterLast(".")
)