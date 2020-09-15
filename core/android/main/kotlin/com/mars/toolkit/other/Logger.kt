@file:Suppress("FunctionName")

package com.mars.toolkit.other

import android.util.Log
import com.mars.toolkit.iterations.joinToString
import com.mars.toolkit.other.Logger.enable
import com.mars.toolkit.other.Logger.loggerTag
import com.mars.toolkit.unsupported
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.*


object Logger {
  /** Global logger tag */
  var loggerTag = "MarsLogger"

  /** Decide whether the log is print */
  var enable = true

  fun init(adapter: LogAdapter = AndroidLogAdapter()) {
    addLogAdapter(adapter)
  }
}

/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 */
fun logError(msg: Any?, tag: String = loggerTag) = logError(null, msg, tag)

/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logError(throwable: T?, msg: Any? = "Error", tag: String = loggerTag) =
  _log(ERROR, tag, msg._resolve(), throwable)

/**
 * Similar as [Log.e].
 * Convert [msg] to String by [_resolve]
 * ```
 * logError(throwable) { "what the fuck error?" }
 * ```
 */
inline fun logError(throwable: Throwable?, msg: () -> Any?) =
  logError(throwable, msg())


/**
 * Similar as [Log.w].
 * Convert [msg] to String by [_resolve]
 */
fun logWarn(msg: Any?, tag: String = loggerTag) = logWarn(null, msg, tag)

/**
 * Similar as [Log.w].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logWarn(throwable: T?, msg: Any? = "Warning", tag: String = loggerTag) =
  _log(WARN, tag, msg._resolve(), throwable)

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
fun logInfo(msg: Any?, tag: String = loggerTag) = logInfo(null, msg, tag)

/**
 * Similar as [Log.i].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logInfo(throwable: T?, msg: Any? = "Info", tag: String = loggerTag) =
  _log(INFO, tag, msg._resolve(), throwable)

/**
 * Similar as [Log.i].
 * Convert [msg] to String by [_resolve]
 * ```
 * logInfo(throwable) { "what the fuck info?" }
 * ```
 */
inline fun logInfo(throwable: Throwable?, msg: () -> Any?) =
  logInfo(throwable, msg())


/**
 * Similar as [Log.d].
 * Convert [msg] to String by [_resolve]
 */
fun logDebug(msg: Any?, tag: String = loggerTag) = logDebug(null, msg, tag)

/**
 * Similar as [Log.d].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logDebug(throwable: T?, msg: Any? = "Debug", tag: String = loggerTag) =
  _log(DEBUG, tag, msg._resolve(), throwable)

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
fun logVerbose(msg: Any?, tag: String = loggerTag) = logVerbose(null, msg, tag)

/**
 * Similar as [Log.v].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logVerbose(throwable: T?, msg: Any? = "Verbose", tag: String = loggerTag) =
  _log(VERBOSE, tag, msg._resolve(), throwable)

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
fun logWtf(msg: Any?, tag: String = loggerTag) = logWtf(null, msg, tag)

/**
 * Similar as [Log.wtf].
 * Convert [msg] to String by [_resolve]
 */
fun <T : Throwable> logWtf(throwable: T?, msg: Any? = "Assert", tag: String = loggerTag) =
  _log(ASSERT, tag, msg._resolve(), throwable)

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
 * Similar as [Logger.json]
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
inline fun logJson(jsonString: () -> String) = json(jsonString())

/**
 * Similar as [Logger.json]
 * ```
 * logJson("{ \"key\": 3, \"value\": something}")
 * ```
 */
fun logJson(jsonString: String) = json(jsonString)

// Todo: Convert data class to json string and print


/** Similar as [Logger.xml] */
inline fun logXml(xmlString: () -> String) = xml(xmlString())

/** Similar as [Logger.xml] */
fun logXml(xmlString: String) = json(xmlString)


@PublishedApi internal fun _log(
  priority: Int,
  tag: String,
  message: String,
  throwable: Throwable?,
) {
  if (!enable) return
  log(priority, tag, message, throwable)
}


fun Any?._resolve(): String = when {
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