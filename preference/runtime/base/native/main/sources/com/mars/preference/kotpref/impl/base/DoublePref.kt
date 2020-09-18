package com.mars.preference.kotpref.impl.base

import android.content.SharedPreferences
import com.mars.preference.core.get
import com.mars.preference.core.put
import com.mars.preference.kotpref.KotprefModel

internal class DoublePref(
  val default: Double,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Double)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: Double, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<Double>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): Double =
    preference.get(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: Double,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.put(key, value)
}