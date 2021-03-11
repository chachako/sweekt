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

package com.meowbase.ui.extension.coil.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.memory.MemoryCache
import coil.request.*

/*
 * author: 凛
 * date: 2020/9/15 下午8:00
 * github: https://github.com/RinOrz
 * description: 用于 IDE 预览 FIXME https://github.com/coil-kt/coil/issues/535
 */
class FakeImageLoader : ImageLoader {
  override val bitmapPool: BitmapPool = BitmapPool(1)
  override val defaults: DefaultRequestOptions = DefaultRequestOptions.INSTANCE
  override val memoryCache: MemoryCache = object : MemoryCache {
    override val maxSize: Int = 2
    override val size: Int = 1

    override fun clear() {
    }

    override fun get(key: MemoryCache.Key): Bitmap? = null

    override fun remove(key: MemoryCache.Key): Boolean = true

    override fun set(key: MemoryCache.Key, bitmap: Bitmap) {
    }
  }

  override fun enqueue(request: ImageRequest): Disposable = object: Disposable {
    override val isDisposed: Boolean = false

    @ExperimentalCoilApi override suspend fun await() {
    }

    override fun dispose() {
    }
  }

  override suspend fun execute(request: ImageRequest): ImageResult = SuccessResult(
    request = request,
    drawable = ColorDrawable(Color.BLUE),
    metadata = ImageResult.Metadata(null, true, DataSource.MEMORY, false)
  )

  override fun shutdown() {
  }
}