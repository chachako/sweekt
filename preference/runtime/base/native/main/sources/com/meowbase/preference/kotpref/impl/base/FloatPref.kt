package com.meowbase.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.meowbase.preference.kotpref.KotprefModel

class FloatPref(
  val default: Float,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Float)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: Float, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<Float>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): Float =
    preference.getFloat(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: Float,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putFloat(key, value)
}
