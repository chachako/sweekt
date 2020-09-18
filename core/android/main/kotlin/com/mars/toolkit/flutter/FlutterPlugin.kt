@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.toolkit.flutter

import com.mars.toolkit.nullPointer
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/*
 * author: 凛
 * date: 2020/9/9 下午7:49
 * github: https://github.com/oh-Rin
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