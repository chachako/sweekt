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

package com.meowbase.toolkit.io

import coil.util.CoilUtils
import okhttp3.Cache
import java.io.File
import com.meowbase.toolkit.appContext

/**
 * 解析并返回 Coil 的缓存图片文件
 * [Modified](https://github.com/coil-kt/coil/issues/528)
 *
 * @param url 需要解析的图片 HttpUrl
 * @see Cache.resolveFile
 */
fun resolveCoilCache(url: String, cache: Cache = CoilUtils.createDefaultCache(appContext)): File =
  cache.resolveFile(url)