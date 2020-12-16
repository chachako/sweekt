package com.meowbase.preference.mmkv

import com.meowbase.preference.flupref.FluprefModelProvider
import com.meowbase.preference.kotpref.KotprefModel

/*
 * author: 凛
 * date: 2020/9/18 下午12:33
 * github: https://github.com/RinOrz
 * description: 支持 Flutter 端的调用
 */
class FlutterMmkv: FluprefModelProvider {
  init {
    initializeMMKV()
  }

  /** Flutter 端发起请求时的需要响应的名称 */
  override fun requestName(): String = "provideMmkv"

  /** 提供一个请求响应后返回到 Flutter 端以供使用的实例 */
  override fun kotprefProvider(
    arguments: List<*>,
    name: String,
    mode: Int,
    commitAllProperties: Boolean
  ): KotprefModel = MmkvModel(
    name = name,
    processMode = mode,
    cryptKey = arguments[3] as String?,
    savePath = arguments[4] as String?,
    commitAllProperties = commitAllProperties
  )
}