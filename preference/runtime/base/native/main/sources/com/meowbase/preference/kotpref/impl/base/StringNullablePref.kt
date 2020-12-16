package com.meowbase.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.meowbase.preference.kotpref.KotprefModel

class StringNullablePref(
  val default: String?,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> String?)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: String?, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<String?>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): String? =
    preference.getString(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: String?,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putString(key, value)
}
