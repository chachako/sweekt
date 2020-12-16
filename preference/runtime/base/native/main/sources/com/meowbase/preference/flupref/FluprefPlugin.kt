@file:Suppress("UNCHECKED_CAST")

package com.meowbase.preference.flupref

import android.annotation.SuppressLint
import android.content.SharedPreferences.Editor
import com.meowbase.preference.core.*
import com.meowbase.preference.kotpref.KotprefModel
import com.meowbase.preference.kotpref.impl.base.AbstractPref
import com.meowbase.toolkit.flutter.FlutterPlugin
import com.meowbase.toolkit.flutter.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class FluprefPlugin : FlutterPlugin(), MethodChannel.MethodCallHandler {
  private var model: KotprefModel? = null

  override fun onAttachedToEngine(binding: FlutterPluginBinding) {
    methodChannel = MethodChannel(binding.binaryMessenger, "meowbase.flutter.plugins/flupref")
    methodChannel?.setMethodCallHandler(this)
  }

  override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    super.onDetachedFromEngine(binding)
    model = null
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    // ensure initialized the real preferenceInstance
    newInstance(call, result)
    // operating the preferences properties
    updatePreference(call, result)
  }

  private fun newInstance(call: MethodCall, result: MethodChannel.Result) {
    // 只有 flutter 端请求创建实例时才会执行下面方法，否则直接 return
    val arguments = call.arguments as? List<*> ?: return
    val name = arguments[0] as String
    val mode = arguments[1] as Int
    val commitAllProperties = arguments[2] as Boolean
    model = when (call.method) {
      "provideSharedPref" -> SharedPrefModel(
        name = name,
        mode = mode,
        commitAllProperties = commitAllProperties
      )
      else -> {
        val provider = kotprefProviders[call.method]
        if (provider == null) {
          result.success(false)
          error("Please provide the prefModel info!")
        }
        // use preference extensions
        provider(arguments, name, mode, commitAllProperties)
      }
    }
    result.success(true)
  }

  @SuppressLint("ApplySharedPref", "CommitPrefEdits")
  private fun updatePreference(call: MethodCall, result: MethodChannel.Result) {
    // 只有 flutter 端请求更新 pref 时才会执行下面方法，否则直接 return
    val arguments = call.arguments as? Map<*, *> ?: return
    val model = this.model!!
    val editor = model.run {
      beginBatchEdit()
      kotprefEditor!!
    }

    val key = arguments["key"] as String
    // save value from flutter dynamic property
    val value = arguments["value"]
    val synchronous = arguments["synchronous"] as Boolean? ?: false

    fun Editor.submit() = when {
      synchronous -> commit()
      else -> {
        apply()
        true
      }
    }

    fun <T> AbstractPref<T>.get() = get(model, model.preferences)

    fun <T> AbstractPref<T>.submit(value: Any?) = put(model, value as T, editor).submit()

    val send: Any? = when (call.method) {
      "getDouble" -> model.double(value as? Double ?: .0, key).get()
      "getByteArray" -> model.bytesNullable(value as? ByteArray, key).get()
      "getLong" -> model.long(value as? Long ?: 0, key).get()
      "getBoolean" -> model.boolean(value as? Boolean ?: false, key).get()
      "getFloat" -> model.float(value as? Float ?: 0f, key).get()
      "getInt" -> model.int(value as? Int ?: 0, key).get()
      "getString" -> model.stringNullable(value as String?, key).get()
      "getStringSet" -> model.stringSet((value as? List<String>)?.toSet() ?: emptySet(), key)
        .get(model)

      "putDouble" -> model.double(key = key).submit(value)
      "putByteArray" -> model.bytesNullable(key = key).submit(value)
      "putLong" -> model.long(key = key).submit(value)
      "putBoolean" -> model.boolean(key = key).submit(value)
      "putFloat" -> model.float(key = key).submit(value)
      "putInt" -> model.int(key = key).submit(value)
      "putString" -> model.string(key = key).submit(value as String?)
      "putStringSet" -> model.stringSet((value as? List<String>)?.toSet() ?: emptySet(), key)
        .get(model).addAll(value as Collection<String>)

      "remove" -> editor.remove(key).submit()
      "clear" -> editor.clear().submit()
      else -> result.notImplemented()
    }
    result.success(send)
    model.cancelBatchEdit()
  }
}