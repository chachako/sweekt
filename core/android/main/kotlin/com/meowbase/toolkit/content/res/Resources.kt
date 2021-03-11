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

@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.meowbase.toolkit.content.res

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.meowbase.toolkit.data.ContextProvider

// Drawable

/** 根据 [id] 获取一个 [Drawable] 资源，或者 null */
fun Context.drawableResourceOrNull(@DrawableRes id: Int): Drawable? =
  ContextCompat.getDrawable(this, id)

fun View.drawableResourceOrNull(@DrawableRes id: Int): Drawable? =
  context.drawableResourceOrNull(id)


/**
 * 根据 [id] 获取一个 [Drawable] 资源
 * @throws IllegalStateException 获取失败抛出异常 [kotlin.error]
 */
fun Context.drawableResource(@DrawableRes id: Int): Drawable =
  drawableResourceOrNull(id) ?: error("无法获取 id 为 $id 的 Drawable 资源！")

fun View.drawableResource(@DrawableRes id: Int): Drawable =
  context.drawableResource(id)


// Color

/** 根据 [id] 获取一个 Color 资源，或者 null */
@ColorInt fun Context.colorResourceOrNull(@ColorRes id: Int): Int? =
  ContextCompat.getColor(this, id)

@ColorInt fun View.colorResourceOrNull(@ColorRes id: Int): Int? =
  context.colorResourceOrNull(id)


/**
 * 根据 [id] 获取一个 Color 资源
 * @throws IllegalStateException 获取失败抛出异常 [kotlin.error]
 */
@ColorInt fun Context.colorResource(@ColorRes id: Int): Int =
  colorResourceOrNull(id) ?: error("无法获取 id 为 $id 的 Drawable 资源！")

@ColorInt fun View.colorResource(@ColorRes id: Int): Int =
  context.colorResource(id)


// String

/** 根据 [id] 获取一个 [String] 资源，或者 null */
fun Context.stringResourceOrNull(@StringRes id: Int): String? =
  getString(id)

fun View.stringResourceOrNull(@StringRes id: Int): String? =
  context.stringResourceOrNull(id)


/**
 * 根据 [id] 获取一个 [String] 资源
 * @throws IllegalStateException 获取失败抛出异常 [kotlin.error]
 */
fun Context.stringResource(@StringRes id: Int): String =
  stringResourceOrNull(id) ?: error("无法获取 id 为 $id 的 Drawable 资源！")

fun View.stringResource(@StringRes id: Int): String =
  context.stringResource(id)


/**
 * 根据 [id] 获取一个 [T] 类型的资源
 * @throws IllegalStateException 资源类型错误抛出异常 [kotlin.error]
 */
inline fun <reified T> Context.resource(@DrawableRes id: Int): T = when (T::class) {
  Drawable::class -> drawableResource(id)
  String::class -> stringResource(id)
  Int::class -> colorResource(id)
  else -> error("无法获取资源，未知类型 ${T::class.java.name}")
} as T

inline fun <reified T> View.resource(@DrawableRes id: Int): T = context.resource(id)