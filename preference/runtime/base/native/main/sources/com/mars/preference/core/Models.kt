package com.mars.preference.core

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.mars.preference.kotpref.KotprefModel
import com.mars.toolkit.appContext

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
