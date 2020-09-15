package com.mars.toolkit.widget

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
