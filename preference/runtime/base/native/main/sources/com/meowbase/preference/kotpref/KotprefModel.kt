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

package com.meowbase.preference.kotpref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build
import android.os.SystemClock
import androidx.annotation.CallSuper
import com.meowbase.preference.core.PreferencesProvider
import com.meowbase.preference.kotpref.impl.base.*
import com.meowbase.toolkit.appContext
import java.util.*

abstract class KotprefModel(
  /** Preference file name */
  name: String? = null,
  /** Some custom preferences may not need mode */
  mode: Int?,
  /** Extra arguments of preferences instance */
  private val extraArgs: Map<String, Any?>?,
  /** Preferences provider, the provide result must be based on [SharedPreferences] */
  private val provider: PreferencesProvider,
  /** [Editor.commit] all properties in this pref by default instead of [Editor.apply] */
  val commitAllProperties: Boolean = false,
  /** Some custom preferences may not need [Context] */
  val context: Context? = appContext
) {
  /**
   * Wait for batch editing and submit once
   */
  internal var kotprefInEditing: Boolean = false
  internal var kotprefStartEditTime: Long = Long.MAX_VALUE

  /**
   * Internal shared preferences.
   * This property will be initialized on use.
   */
  internal val kotprefPreference: KotprefPreferences by lazy {
    KotprefPreferences(provider.get(context, name!!, mode, extraArgs))
  }

  /**
   * Internal shared preferences editor.
   */
  internal var kotprefEditor: KotprefPreferences.KotprefEditor? = null

  /**
   * SharedPreferences instance exposed.
   * Use carefully when during bulk edit, it may cause inconsistent with internal data of Kotpref.
   */
  val preferences: SharedPreferences
    get() = kotprefPreference

  /**
   * Clear all preferences in this model
   */
  @CallSuper
  open fun clear() {
    beginBatchEdit()
    kotprefEditor!!.clear()
    commitBatchEdit()
  }

  /**
   * Delegate string shared preferences property.
   * @param default default string value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun string(
    default: String = "",
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<String> = StringPref(default, key, commitByDefault)

  /**
   * Delegate nullable string shared preferences property.
   * @param default default string value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun stringNullable(
    default: String? = null,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<String?> = StringNullablePref(default, key, commitByDefault)

  /**
   * Delegate int shared preferences property.
   * @param default default int value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun int(
    default: Int = 0,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<Int> = IntPref(default, key, commitByDefault)

  /**
   * Delegate long shared preferences property.
   * @param default default long value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun long(
    default: Long = 0L,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<Long> = LongPref(default, key, commitByDefault)

  /**
   * Delegate float shared preferences property.
   * @param default default float value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun float(
    default: Float = 0F,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<Float> = FloatPref(default, key, commitByDefault)

  /**
   * Delegate double shared preferences property.
   * @param default default double value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun double(
    default: Double = 0.0,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<Double> = DoublePref(default, key, commitByDefault)

  /**
   * Delegate bytes shared preferences property.
   * @param default default bytes value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun bytes(
    default: ByteArray = byteArrayOf(),
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<ByteArray> = BytesPref(default, key, commitByDefault)

  /**
   * Delegate nullable bytes shared preferences property.
   * @param default default bytes value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun bytesNullable(
    default: ByteArray? = null,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<ByteArray?> = BytesNullablePref(default, key, commitByDefault)

  /**
   * Delegate boolean shared preferences property.
   * @param default default boolean value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  open fun boolean(
    default: Boolean = false,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): AbstractPref<Boolean> = BooleanPref(default, key, commitByDefault)

  /**
   * Delegate string set shared preferences property.
   * @param default default string set value
   * @param key custom preferences key
   * @param commitByDefault commit this property instead of apply
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  open fun stringSet(
    default: Set<String> = LinkedHashSet(),
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): StringSetPref = StringSetPref(default, key, commitByDefault)

  open fun stringSet(
    vararg default: String,
    key: String? = null,
    commitByDefault: Boolean = commitAllProperties
  ): StringSetPref = StringSetPref(default.toMutableSet(), key, commitByDefault)

  /**
   * Begin bulk edit mode. You must commit or cancel after bulk edit finished.
   */
  @SuppressLint("CommitPrefEdits")
  fun beginBatchEdit() {
    kotprefInEditing = true
    kotprefStartEditTime = SystemClock.uptimeMillis()
    kotprefEditor = kotprefPreference.KotprefEditor(kotprefPreference.edit())
  }

  /**
   * Commit values set in the bulk edit mode to preferences, background mode.
   */
  fun applyBatchEdit() {
    kotprefEditor!!.apply()
    kotprefInEditing = false
  }

  /**
   * Commit values set in the bulk edit mode to preferences immediately, in blocking manner.
   */
  fun commitBatchEdit() {
    kotprefEditor!!.commit()
    kotprefInEditing = false
  }

  /**
   * Cancel bulk edit mode. Values set in the bulk mode will be rolled back.
   */
  fun cancelBatchEdit() {
    kotprefEditor = null
    kotprefInEditing = false
  }
}