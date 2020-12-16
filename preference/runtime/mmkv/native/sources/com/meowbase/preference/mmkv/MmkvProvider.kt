package com.meowbase.preference.mmkv

import android.content.Context
import android.content.SharedPreferences
import com.meowbase.preference.core.PreferencesProvider
import com.tencent.mmkv.MMKV

/** internal [MMKV] provider */
internal object MmkvProvider : PreferencesProvider {
  const val CRYPT_KEY = "_"
  const val RELATIVE_PATH = "__"
  override fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences = MMKV.mmkvWithID(
    name,
    mode!!,
    extras!![CRYPT_KEY] as String?,
    extras[RELATIVE_PATH] as String?
  ) ?: error(
    "initialize MMKV instance failed! name: $name, mode: $mode, " +
      extras.map { "${it.key}: ${it.value}" }.joinToString()
  )
}