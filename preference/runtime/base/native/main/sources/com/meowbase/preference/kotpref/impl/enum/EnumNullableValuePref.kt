package com.meowbase.preference.kotpref.impl.enum

import android.content.SharedPreferences
import com.meowbase.preference.kotpref.KotprefModel
import com.meowbase.preference.kotpref.impl.base.AbstractPref
import kotlin.reflect.KClass

class EnumNullableValuePref<T : Enum<*>>(
  enumClass: KClass<T>,
  private val default: T?,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> T?)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: T?, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<T?>() {
  private val enumConstants = enumClass.java.enumConstants

  override fun get(thisRef: KotprefModel, preference: SharedPreferences): T? =
    enumConstants?.firstOrNull { it.name == preference.getString(key, default?.name) }

  override fun put(
    thisRef: KotprefModel,
    value: T?,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putString(key, value?.name)
}
