package com.meowbase.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.meowbase.preference.kotpref.KotprefModel

class IntPref(
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
