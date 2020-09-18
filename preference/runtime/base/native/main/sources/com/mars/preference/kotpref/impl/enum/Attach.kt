package com.mars.preference.kotpref.impl.enum

import com.mars.preference.kotpref.KotprefModel
import com.mars.preference.kotpref.impl.base.AbstractPref

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
inline fun <reified T : Enum<*>> KotprefModel.nullableEnumValue(
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