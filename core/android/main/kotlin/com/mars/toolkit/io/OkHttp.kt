package com.mars.toolkit.io

import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.File

/**
 * 解析并返回的缓存后的 [url] 文件
 *
 * @param url 需要解析的 HttpUrl
 * @receiver OkHttp 的缓存 [Cache]
 */
fun Cache.resolveFile(url: String): File = File(directory, Cache.key(url.toHttpUrl()))