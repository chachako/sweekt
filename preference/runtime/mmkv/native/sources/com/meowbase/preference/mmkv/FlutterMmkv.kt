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