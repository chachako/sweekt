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

package com.meowbase.preference.core

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.meowbase.preference.kotpref.KotprefModel
import com.meowbase.toolkit.appContext

/**
 * Definition the model using system [SharedPreferences]
 * @see SharedPreferencesProvider
 */
open class SharedPrefModel(
  /** Preference file name */
  name: String? = null,
  /** Preference read/write mode */
  mode: Int = Context.MODE_PRIVATE,
  /** [Editor.commit] all properties in this pref by default instead of [Editor.apply] */
  commitAllProperties: Boolean = false,
  /** System preferences must be have [Context] */
  context: Context = appContext
) : KotprefModel(name, mode, null, SharedPreferencesProvider, commitAllProperties, context)
