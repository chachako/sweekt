package com.mars.preference.kotpref.impl.base

import android.content.SharedPreferences
import com.mars.preference.kotpref.KotprefModel

internal class LongPref(
  val default: Long,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Long)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: Long, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<Long>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): Long =
    preference.getLong(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: Long,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putLong(key, value)
}
