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

import com.meowbase.preference.kotpref.KotprefModel
import com.meowbase.preference.kotpref.impl.base.AbstractPref

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValue(
  default: T,
  key: String? = null,
  commitByDefault: Boolean = commitAllProperties
): AbstractPref<T> = EnumValuePref(T::class, default, key, commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValueNullable(
  default: T? = null,
  key: String? = null,
  commitByDefault: Boolean = commitAllProperties
): AbstractPref<T?> = EnumNullableValuePref(T::class, default, key, commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's ordinal as an integer.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumOrdinal(
  default: T,
  key: String? = null,
  commitByDefault: Boolean = commitAllProperties
): AbstractPref<T> = EnumOrdinalPref(T::class, default, key, commitByDefault)