package com.mars.preference.core

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble

/**
 * Extension to choose between {@link SharedPreferences.Editor.commit} and {@link SharedPreferences.Editor.apply}
 * @param synchronous save to sharedPref file instantly
 */
fun Editor.execute(synchronous: Boolean) {
  if (synchronous) commit() else apply()
}


/**
 * Value transformers
 */

fun SharedPreferences.get(key: String?, default: Double) =
  longBitsToDouble(getLong(key, doubleToRawLongBits(default)))

fun Editor.put(key: String?, value: Double): Editor =
  putLong(key, doubleToRawLongBits(value))

fun SharedPreferences.get(key: String?, default: ByteArray?) =
  requireNotNull(getString(key, default?.let {
    String(it, Charsets.ISO_8859_1)
  })).toByteArray(Charsets.ISO_8859_1)

fun Editor.put(key: String?, value: ByteArray?): Editor =
  putString(key, value?.let { String(it, Charsets.ISO_8859_1) })