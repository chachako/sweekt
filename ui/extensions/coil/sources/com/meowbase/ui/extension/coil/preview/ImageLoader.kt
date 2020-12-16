@file:Suppress("SpellCheckingInspection")

package com.meowbase.ui.extension.coil.preview

import android.content.Context
import coil.Coil
import coil.ImageLoader

val Context.defaultImageLoader get(): ImageLoader =
  if (this.javaClass.name == "com.android.layoutlib.bridge.android.BridgeContext") {
    FakeImageLoader()
  } else {
    Coil.imageLoader(this)
  }
