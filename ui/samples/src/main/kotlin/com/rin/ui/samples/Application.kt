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

package com.rin.ui.samples

import android.app.Application
import com.meowbase.toolkit.other.Logger
import com.rin.ui.samples.base.Sample
import com.rin.ui.samples.ebook.Ebook
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/9/12 下午5:25
 */
class Application : Application() {
  override fun onCreate() {
    super.onCreate()
    Logger.init()
    // 注入全局 Context
    startKoin { androidContext(this@Application) }
    // 加载所有 App 示例
    load(
      Ebook(),
    )
  }

  private fun load(vararg samples: Sample) {
    samples.forEach {
      it.onCreate()
    }
  }
}