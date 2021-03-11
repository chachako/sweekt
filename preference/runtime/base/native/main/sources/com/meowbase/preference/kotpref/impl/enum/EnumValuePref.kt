/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.preference.kotpref.impl.enum

import android.content.SharedPreferences
import com.meowbase.preference.kotpref.KotprefModel
import com.meowbase.preference.kotpref.impl.base.AbstractPref
import kotlin.reflect.KClass

class EnumValuePref<T : Enum<*>>(
  enumClass: KClass<T>,
  val default: T,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> T)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: T, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<T>() {
  private val enumConstants = enumClass.java.enumConstants

  override fun get(thisRef: KotprefModel, preference: SharedPreferences): T =
    enumConstants!!.first { it.name == preference.getString(key, default.name) }

  override fun put(
    thisRef: KotprefModel,
    value: T,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.putString(key, value.name)
}
