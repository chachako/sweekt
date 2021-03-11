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

package com.meowbase.preference.mmkv

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.meowbase.toolkit.appContext
import com.meowbase.toolkit.appDebugging
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