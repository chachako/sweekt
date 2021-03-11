/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.ui.util

import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.ModifierManager
import com.meowbase.ui.core.decoupling.ModifierProvider
import com.meowbase.ui.core.graphics.geometry.Size

/**
 * 为任意视图更新修饰符
 *
 * @note Gradle 插件会为所有 View 都注入 [ModifierProvider]
 */
inline val View.size get() = Size(width, height)

/**
 * 为任意视图添加修饰符
 *
 * @note Gradle 插件会为所有 View 都注入 [ModifierProvider]
 */
fun View.withModifier(modifier: Modifier) {
  (this as? ModifierProvider)?.also {
    it.modifier = modifier
  } ?: modifier.apply { realize(parent as? ViewGroup) }
}

/**
 * 为任意视图更新修饰符
 *
 * @note Gradle 插件会为所有 View 都注入 [ModifierProvider]
 */
inline fun View.updateModifier(modifier: Modifier.() -> Unit) =
  withModifier(ModifierManager().apply(modifier))