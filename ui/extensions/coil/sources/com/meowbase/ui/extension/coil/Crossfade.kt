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

@file:Suppress("FunctionName", "EXPERIMENTAL_API_USAGE", "SpellCheckingInspection")

package com.meowbase.ui.extension.coil

import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.OriginalSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import coil.transition.CrossfadeTransition
import com.meowbase.ui.Ui
import com.meowbase.ui.asLayout
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.extension.coil.preview.defaultImageLoader
import com.meowbase.ui.widget.implement.Image

@PublishedApi internal const val DefaultTransitionDuration = 1000


/**
 * 具有淡入淡出动画的 Coil 图片视图
 * @see CoilImage
 * @see CrossfadeTransition
 */
fun Ui.CoilFadeImage(
  data: Any?,
  lazy: Boolean = false,
  modifier: Modifier = Modifier,
  loader: ImageLoader = this.asLayout.context.defaultImageLoader,
  scale: Scale = Scale.FIT,
  size: Size = OriginalSize,
  transformation: Transformation? = null,
  transformations: List<Transformation>? = null,
  fadeDuration: Int = DefaultTransitionDuration,
  placeholder: Drawable? = null,
  error: Drawable? = null,
  fallback: Drawable? = null,
  memoryCache: CachePolicy = CachePolicy.ENABLED,
  diskCache: CachePolicy = CachePolicy.ENABLED,
  networkCache: CachePolicy = CachePolicy.ENABLED,
  allowHardware: Boolean = true,
  allowRgb565: Boolean = false,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
): Image = CoilImage(
  data,
  lazy,
  modifier,
  loader,
  scale,
  size,
  transformation,
  transformations,
  CrossfadeTransition(fadeDuration),
  placeholder,
  error,
  fallback,
  memoryCache,
  diskCache,
  networkCache,
  allowHardware,
  allowRgb565,
  alpha
)


/**
 * 具有淡入淡出动画的 Coil 图片视图
 * @see CoilImage
 * @see CrossfadeTransition
 */
inline fun Ui.CoilFadeImage(
  data: Any?,
  lazy: Boolean = false,
  modifier: Modifier = Modifier,
  loader: ImageLoader = this.asLayout.context.defaultImageLoader,
  fadeDuration: Int = DefaultTransitionDuration,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
  builder: ImageRequest.Builder.() -> Unit,
): Image = CoilImage(data, lazy, modifier, loader, alpha) {
  crossfade(fadeDuration)
  builder()
}