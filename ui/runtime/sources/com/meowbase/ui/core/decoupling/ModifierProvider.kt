package com.meowbase.ui.core.decoupling

import com.meowbase.ui.core.Modifier

/*
 * author: 凛
 * date: 2020/9/27 下午5:46
 * github: https://github.com/RinOrz
 * description: 为 View 提供修饰符的属性更改
 */
interface ModifierProvider {
  var modifier: Modifier
}
