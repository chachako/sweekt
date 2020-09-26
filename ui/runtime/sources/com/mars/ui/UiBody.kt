package com.mars.ui

/*
 * author: 凛
 * date: 2020/9/23 下午8:22
 * github: https://github.com/oh-Rin
 * description: 代表一个 Ui 主体的代码块，在代码块中可以创建每个部分
 */
typealias UIBody = Ui.() -> Unit

/** 判断 [UIBody] 是否未指定 */
inline val UIBody.isUnspecified: Boolean get() = this == Ui.Unspecified

/** 判断 [UIBody] 是否已经指定 */
inline val UIBody.isSpecified: Boolean get() = this != Ui.Unspecified