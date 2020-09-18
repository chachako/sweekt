package com.mars.preference.mmkv

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.mars.toolkit.appContext
import com.mars.toolkit.appDebugging
import java.io.File

/** Make sure initialization once */
internal var initialize = false

/**
 * Auto-call after your crated [MmkvModel] or [FlutterMmkv].
 * If need to custom [MMKV.rootDir], only need to initialization it on [Application]
 */
fun initializeMMKV(mmkvDirectory: File = appContext.filesDir.resolve("mmkv")) {
  if (initialize) return
  mmkvDirectory.apply {
    if (!exists()) mkdirs()
    val result = MMKV.initialize(
      absolutePath,
      if (appDebugging) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone
    )
    initialize = result == absolutePath
  }
}