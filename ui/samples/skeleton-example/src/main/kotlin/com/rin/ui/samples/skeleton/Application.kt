package com.rin.ui.samples.skeleton

import android.app.Application
import com.mars.toolkit.other.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
  }
}