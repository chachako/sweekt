package com.mars.preference.annotations

/*
 * author: 凛
 * date: 2020/9/20 下午12:59
 * github: https://github.com/oh-Rin
 * description: 定义 KotprefModel 是可以被混淆的（模糊偏好文件名和偏好属性名）
 */
@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.SOURCE)
annotation class VaguePreference