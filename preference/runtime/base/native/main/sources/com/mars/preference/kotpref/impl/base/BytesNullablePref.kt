package com.mars.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.mars.preference.core.get
import com.mars.preference.core.put
import com.mars.preference.kotpref.KotprefModel

class BytesNullablePref(
  val default: ByteArray?,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> ByteArray?)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: ByteArray?, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<ByteArray?>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): ByteArray? =
    preference.get(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: ByteArray?,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.put(key, value)
}
