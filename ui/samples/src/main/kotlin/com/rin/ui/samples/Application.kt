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