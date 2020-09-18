package com.mars.preference.kotpref

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import com.mars.preference.kotpref.impl.base.StringSetPref
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