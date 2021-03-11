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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.toolkit.flutter

import com.meowbase.toolkit.nullPointer
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/*
 * author: 凛
 * date: 2020/9/9 下午7:49
 * github: https://github.com/RinOrz
 * description: 减少 FlutterPlugin 的模板代码
 */
abstract class FlutterPlugin : FlutterPlugin {
  protected var eventChannel: EventChannel? = null
    get() = field ?: nullPointer(
      "eventChannel 需要在你的 FlutterPlugin 的 onAttachedToEngine 或其他位置中注入，不可以为空！"
    )

  protected var methodChannel: MethodChannel? = null
    get() = field ?: nullPointer(
      "methodChannel 需要在你的 FlutterPlugin 的 onAttachedToEngine 或其他位置中注入，不可以为空！"
    )

  override fun onAttachedToEngine(binding: FlutterPluginBinding) {}

  override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    methodChannel?.setMethodCallHandler(null)
    methodChannel = null
    eventChannel?.setStreamHandler(null)
    eventChannel = null
  }
}

typealias FlutterPluginBinding = FlutterPlugin.FlutterPluginBinding