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

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import com.meowbase.preference.kotpref.impl.base.StringSetPref
import java.util.HashMap

internal class KotprefPreferences(
  private val preferences: SharedPreferences
) : SharedPreferences by preferences {

  override fun edit(): SharedPreferences.Editor {
    return KotprefEditor(preferences.edit())
  }

  internal inner class KotprefEditor(
    val editor: SharedPreferences.Editor
  ) : SharedPreferences.Editor by editor {

    private val prefStringSet: MutableMap<String, StringSetPref.PrefMutableSet> by lazy {
      HashMap<String, StringSetPref.PrefMutableSet>()
    }

    override fun apply() {
      syncTransaction()
      editor.apply()
    }

    override fun commit(): Boolean {
      syncTransaction()
      return editor.commit()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    internal fun putStringSet(
      key: String,
      prefSet: StringSetPref.PrefMutableSet
    ): SharedPreferences.Editor {
      prefStringSet[key] = prefSet
      return this
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun syncTransaction() {
      prefStringSet.keys.forEach { key ->
        prefStringSet[key]?.let {
          editor.putStringSet(key, it)
          it.syncTransaction()
        }
      }
      prefStringSet.clear()
    }
  }
}