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

package com.meowbase.toolkit.widget

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat

@PublishedApi internal val mResource by lazy { ImageView::class.java.getDeclaredField("mResource") }

inline var ImageView.imageResource: Int
  get() = mResource.get(this) as Int
  set(@DrawableRes value) = setImageResource(value)

inline var ImageView.imageDrawable: Drawable?
  get() = drawable
  set(value) = setImageDrawable(value)

inline var ImageView.imageBitmap: Bitmap?
  get() = (drawable as? BitmapDrawable)?.bitmap
  set(value) = setImageBitmap(value)

inline var ImageView.imageTint: Int?
  get() = ImageViewCompat.getImageTintList(this)?.defaultColor
  set(value) = ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(value!!))
