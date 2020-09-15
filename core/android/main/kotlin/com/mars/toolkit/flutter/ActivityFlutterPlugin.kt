@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.toolkit.flutter

import android.app.Activity
import com.mars.toolkit.flutter.FlutterPlugin as OpenFlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/*
 * author: 凛
 * date: 2020/9/9 下午7:49
 * github: https://github.com/oh-Rin
 * description: 减少具有 ActivityAware 接口的 FlutterPlugin 的模板代码
 */
abstract class ActivityFlutterPlugin : OpenFlutterPlugin(), ActivityAware {
  protected var activity: Activity? = null
  protected var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    pluginBinding = binding
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) =
    onAttachedToActivity(binding)

  override fun onDetachedFromActivityForConfigChanges() = onDetachedFromActivity()

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    super.onDetachedFromEngine(binding)
    pluginBinding = null
  }
}