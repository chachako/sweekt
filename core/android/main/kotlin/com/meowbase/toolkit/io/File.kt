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