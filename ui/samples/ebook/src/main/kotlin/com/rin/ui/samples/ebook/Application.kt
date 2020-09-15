package com.rin.ui.samples.ebook

import android.app.Application
import com.mars.toolkit.graphics.loadTypeface
import com.mars.toolkit.other.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.reflect.KProperty0

/*
 * author: 凛
 * date: 2020/9/12 下午5:25
 * github: https://github.com/oh-Rin
 */
class Application : Application() {
  override fun onCreate() {
    super.onCreate()
    Logger.init()
    // 注入全局 Context
    startKoin { androidContext(this@Application) }
    // 加载字体
    loadTypeface(Fonts.SectraFine)
    loadTypeface(Fonts.Montserrat.Medium)
    loadTypeface(Fonts.Montserrat.Regular)
  }

  private fun loadTypeface(path: String) =
    loadTypeface(name = path, path = path)
}