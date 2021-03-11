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

@file:Suppress("FunctionName")

package com.meowbase.toolkit.other

import android.util.Log
import com.meowbase.toolkit.BuildConfig
import com.meowbase.toolkit.appDebugging
import com.meowbase.toolkit.iterations.joinToString
import com.meowbase.toolkit.unsupported
import com.orhanobut.logger.*
import com.orhanobut.logger.Logger.*
import com.orhanobut.logger.Logger as RealLogger


object Logger {
  /** Decide whether the log is print */
  var enable: Boolean = appDebugging

  private var initialized = false

  /**
   * Initialize logger with [adapter] or default [AndroidLogAdapter]
   *
   * @param tag Global tag for every log
   * @param showThreadInfo Whether to show thread info or not
   * @param methodCount How many method line to show
   * @param methodOffset Hides internal method calls up to offset
   */
  fun init(
    tag: String = "MeowbaseLogger",
    showThreadInfo: Boolean = false,
    methodCount: Int = 2,
    methodOffset: Int = 2,
    adapter: LogAdapter? = null,
  ) {
    initialized = true
    addLogAdapter(adapter ?: InternalLogAdapter(tag, showThreadInfo, methodCount, methodOffset))
  }

  @PublishedApi internal fun ensureInit() {
    if (!initialized) init()
  }

  private class InternalLogAdapter(
    tag: String,
    showThreadInfo: Boolean,
    methodCount: Int,
    methodOffset: Int
  ) : AndroidLogAdapter(
    PrettyFormatStrategy.newBuilder()
      .showThreadInfo(showThreadInfo)
      .methodCount(methodCount)
      .methodOffset(methodOffset)
      .tag(tag)
      .build()
  ) {
    override fun isLoggable(priority: Int, tag: String?): Boolean = enable
  }
}


/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 */
fun logError(msg: Any?, tag: String? = null) = logError(null, msg, tag)

/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logError(throwable: T?, msg: Any? = "Error", tag: String? = null) {
  Logger.ensureInit()
  log(ERROR, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 * ```
 * logError(throwable) { "what the fuck error?" }
 * ```
 */
inline fun logError(throwable: Throwable?, msg: () -> Any?) = logError(throwable, msg())


/**
 * Similar as [Log.w].
 * Convert [msg] to String by [_resolve]
 */
fun logWarn(msg: Any?, tag: String? = null) = logWarn(null, msg, tag)

/**
 * Similar as [Log.w].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logWarn(throwable: T?, msg: Any? = "Warning", tag: String? = null) {
  Logger.ensureInit()
  log(WARN, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.w].
 * Convert [msg] to String by [_resolve]
 * ```
 * logError(throwable) { "what the fuck error?" }
 * ```
 */
inline fun logWarn(throwable: Throwable?, msg: () -> Any?) =
  logWarn(throwable, msg())


/**
 * Similar as [Log.i].
 * Convert [msg] to String by [_resolve]
 */
fun logInfo(msg: Any?, tag: String? = null) = logInfo(null, msg, tag)

/**
 * Similar as [Log.i].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logInfo(throwable: T?, msg: Any? = "Info", tag: String? = null) {
  Logger.ensureInit()
  log(INFO, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.i].
 * Convert [msg] to String by [_resolve]
 * ```
 * logInfo(throwable) { "what the fuck info?" }
 * ```
 */
inline fun logInfo(throwable: Throwable?, msg: () -> Any?) = logInfo(throwable, msg())


/**
 * Similar as [Log.d].
 * Convert [msg] to String by [_resolve]
 */
fun logDebug(msg: Any?, tag: String? = null) = logDebug(null, msg, tag)

/**
 * Similar as [Log.d].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logDebug(throwable: T?, msg: Any? = "Debug", tag: String? = null) {
  Logger.ensureInit()
  log(DEBUG, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.d].
 * Convert [msg] to String by [_resolve]
 * ```
 * logDebug(throwable) { "what the fuck debug?" }
 * ```
 */
inline fun logDebug(throwable: Throwable?, msg: () -> Any?) =
  logDebug(throwable, msg())


/**
 * Similar as [Log.v].
 * Convert [msg] to String by [_resolve]
 */
fun logVerbose(msg: Any?, tag: String? = null) = logVerbose(null, msg, tag)

/**
 * Similar as [Log.v].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logVerbose(throwable: T?, msg: Any? = "Verbose", tag: String? = null) {
  Logger.ensureInit()
  log(VERBOSE, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.v].
 * Convert [msg] to String by [_resolve]
 * ```
 * logVerbose(throwable) { "what the fuck verbose?" }
 * ```
 */
inline fun logVerbose(throwable: Throwable?, msg: () -> Any?) =
  logVerbose(throwable, msg())


/**
 * Similar as [Log.wtf].
 * Convert [msg] to String by [_resolve]
 */
fun logWtf(msg: Any?, tag: String? = null) = logWtf(null, msg, tag)

/**
 * Similar as [Log.wtf].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logWtf(throwable: T?, msg: Any? = "Assert", tag: String? = null) {
  Logger.ensureInit()
  log(ASSERT, tag, msg._resolve(), throwable)
}

/**
 * Similar as [Log.wtf].
 * Convert [msg] to String by [_resolve]
 * ```
 * logWtf(throwable) { "what the fuck assert?" }
 * ```
 */
inline fun logWtf(throwable: Throwable?, msg: () -> Any?) =
  logWtf(throwable, msg())


/**
 * Similar as [RealLogger.json]
 * ```
 * logJson {
 *   """
 *     {
 *       "key": 1,
 *       "value": true
 *     }
 *   """
 * }
 * ```
 */
inline fun logJson(jsonString: () -> String) = logJson(jsonString())

/**
 * Similar as [RealLogger.json]
 * ```
 * logJson("{ \"key\": 3, \"value\": something}")
 * ```
 */
fun logJson(jsonString: String) {
  Logger.ensureInit()
  json(jsonString)
}

// Todo: Convert data class to json string and print


/** Similar as [RealLogger.xml] */
inline fun logXml(xmlString: () -> String) = logXml(xmlString())

/** Similar as [RealLogger.xml] */
fun logXml(xmlString: String) {
  Logger.ensureInit()
  xml(xmlString)
}


private fun Any?._resolve(): String = when {
  this == null -> "null"
  !this.javaClass.isArray -> toString()
  this is BooleanArray -> joinToString()
  this is ByteArray -> joinToString()
  this is CharArray -> joinToString()
  this is ShortArray -> joinToString()
  this is IntArray -> joinToString()
  this is LongArray -> joinToString()
  this is FloatArray -> joinToString()
  this is DoubleArray -> joinToString()
  this is Array<*> -> joinToString()
  this is Iterable<*> -> joinToString()
  this is Map<*, *> -> joinToString()
  else -> unsupported("不支持将 ${this.javaClass.name} 转换为 String!")
}