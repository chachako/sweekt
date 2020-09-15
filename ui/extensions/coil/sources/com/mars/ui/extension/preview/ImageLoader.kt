@file:Suppress("SpellCheckingInspection")

package com.mars.ui.extension.preview

import android.content.Context
import coil.Coil
import coil.ImageLoader

val Context.defaultImageLoader get(): ImageLoader =
  if (this.javaClass.name == "com.android.layoutlib.bridge.android.BridgeContext") {
    FakeImageLoader()
  } else {
    Coil.imageLoader(this)
  }
