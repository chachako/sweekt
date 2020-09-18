package com.mars.preference.core

import android.content.Context
import android.content.SharedPreferences
import com.mars.toolkit.appContext

/** provide [SharedPreferences] */
interface PreferencesProvider {
  /** return type must be [SharedPreferences.Editor] */
  fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences
}

/** internal [SharedPreferences] provider */
internal object SharedPreferencesProvider : PreferencesProvider {
  override fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences = (context ?: appContext).getSharedPreferences(name, mode!!)
}