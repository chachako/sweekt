package com.mars.ui.util

import android.view.View
import android.view.ViewGroup
import com.mars.ui.core.Modifier
import com.mars.ui.foundation.ModifierProvider

/**
 * 为视图添加修饰符
 * NOTE：Gradle 插件会为所有 View 都注入 [ModifierProvider]
 */
fun View.withModifier(modifier: Modifier) {
  (this as? ModifierProvider)?.also {
    it.modifier = modifier
  } ?: modifier.apply { realize(parent as? ViewGroup) }
}