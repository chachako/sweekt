package com.mars.preference.kotpref.impl.base

import android.content.SharedPreferences
import com.mars.preference.kotpref.KotprefModel

internal class IntPref(
  val default: Int,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Int)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: Int, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<Int>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): Int =
    preference.getInt(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: Int,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putInt(key, value)
}
