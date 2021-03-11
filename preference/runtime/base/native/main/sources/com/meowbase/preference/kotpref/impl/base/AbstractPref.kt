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

@file:Suppress("UNCHECKED_CAST", "ApplySharedPref", "CommitPrefEdits")

package com.meowbase.preference.kotpref.impl.base

import android.content.SharedPreferences
import android.os.SystemClock
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.meowbase.preference.core.execute
import com.meowbase.preference.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractPref<T : Any?> : ReadWriteProperty<KotprefModel, T> {

  abstract val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> T)?

  abstract val putterImplProvider: ((thisRef: KotprefModel, value: T, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)?

  abstract val key: String?
  abstract val commitByDefault: Boolean

  /** 防止当批量修改的时候在其他地方调用了此变量可能会导致的数据错乱 */
  private var lastUpdate: Long = 0
  private var editedData: Any? = null

  private var kotprefModel: KotprefModel? = null
  private var cachedLiveData: MutableLiveData<T>? = null
  private val liveData get() = cachedLiveData ?: MutableLiveData<T>().also { cachedLiveData = it }

  override operator fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
    kotprefModel = thisRef
    if (!thisRef.kotprefInEditing) {
      return get(thisRef, thisRef.kotprefPreference)
    }
    if (lastUpdate < thisRef.kotprefStartEditTime) {
      editedData = getterImplProvider?.invoke(
        thisRef, thisRef.kotprefPreference
      ) ?: get(
        thisRef, thisRef.kotprefPreference
      )
      lastUpdate = SystemClock.uptimeMillis()
    }
    return editedData as T
  }

  override operator fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
    cachedLiveData?.postValue(value!!)
    if (thisRef.kotprefInEditing) {
      editedData = value
      lastUpdate = SystemClock.uptimeMillis()
      putterImplProvider?.invoke(
        thisRef, value, thisRef.kotprefEditor!!
      ) ?: put(
        thisRef, value, thisRef.kotprefEditor!!
      )
    } else {
      val editor = putterImplProvider?.invoke(
        thisRef, value, thisRef.kotprefPreference.edit()
      ) ?: put(
        thisRef, value, thisRef.kotprefPreference.edit()
      )
      editor.execute(commitByDefault)
    }
  }

  abstract fun get(thisRef: KotprefModel, preference: SharedPreferences): T
  abstract fun put(
    thisRef: KotprefModel,
    value: T,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor

  /** remove the this property from [SharedPreferences] */
  fun remove(): Boolean {
    if (kotprefModel!!.kotprefInEditing) {
      editedData = null
      lastUpdate = SystemClock.uptimeMillis()
    }
    val editor = kotprefModel?.kotprefPreference?.edit()?.remove(key)
    return when {
      editor == null -> false
      commitByDefault -> editor.commit()
      else -> {
        editor.apply()
        true
      }
    }
  }

  /** copy from [LiveData] */
  @MainThread open fun observe(owner: LifecycleOwner, observer: Observer<in T?>) =
    liveData.observe(owner, observer)

  @MainThread open fun observeForever(observer: Observer<in T?>) =
    liveData.observeForever(observer)

  @MainThread open fun removeObserver(observer: Observer<in T?>) =
    liveData.removeObserver(observer)

  @MainThread open fun removeObservers(owner: LifecycleOwner) =
    liveData.removeObservers(owner)

  @MainThread open fun hasObservers() =
    liveData.hasObservers()

  @MainThread open fun hasActiveObservers() =
    liveData.hasActiveObservers()
}
