package com.mars.ui.core.decoupling

import com.mars.ui.core.Modifier

/*
 * author: 凛
 * date: 2020/9/27 下午5:46
 * github: https://github.com/oh-Rin
 * description: 为 View 提供修饰符的属性更改
 */
interface ModifierProvider {
  var modifier: Modifier
}
