package com.mars.toolkit.flutter

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import com.mars.toolkit.flutter.FlutterPlugin as OpenFlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel


/**
 * 注册多个 Flutter 插件
 *
 * @param plugin 需要注册的插件
 * @see ActivityFlutterPlugin
 * @see OpenFlutterPlugin
 * @see FlutterPlugin
 */
fun FlutterEngine.registerFlutterPlugins(vararg plugin: FlutterPlugin) = plugin.forEach {
  // 跳过已经注册的插件
  if (!plugins.has(it.javaClass)) {
    plugins.add(it)
  }
}

/**
 * 注册一个 [MethodChannel]
 *
 * @param channel 需要注册的通道名称
 * @param callback Flutter 端调用时的回调
 */
fun FlutterEngine.registerMethodChannel(
  channel: String,
  callback: (String) -> Any?
) = MethodChannel(dartExecutor, channel).setMethodCallHandler { methodCall, result ->
  val success = callback.invoke(methodCall.method)
  if (success == null)
    result.notImplemented()
  else result.success(success)
}

/**
 * 注册多个 [EventChannel]
 *
 * @param channels 需要注册的通道名称
 * @param eventHandler 事件处理回调
 */
fun FlutterEngine.registerEventChannels(
  vararg channels: String,
  eventHandler: EventHandler
) = channels.forEach {
  eventHandler.channel = it
  EventChannel(dartExecutor, it).setStreamHandler(object : EventChannel.StreamHandler {
    override fun onListen(arguments: Any?, eventSink: EventChannel.EventSink?) {
      if (eventSink != null) eventHandler.onListen(arguments, eventSink)
    }
    override fun onCancel(arguments: Any?) = eventHandler.onCancel(arguments)
  })
}


abstract class EventHandler {
  var channel: String = error("意外情况！事件流通道名称未被定义")
    internal set
  abstract fun onListen(arguments: Any?, eventSink: EventChannel.EventSink)
  open fun onCancel(arguments: Any?) {}
}