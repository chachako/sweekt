package com.mars.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.mars.preference.kotpref.KotprefModel

class BooleanPref(
  val default: Boolean,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Boolean)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: Boolean, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<Boolean>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): Boolean =
    preference.getBoolean(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: Boolean,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putBoolean(key, value)
}
