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

@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.toolkit.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

inline fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> = MutableLiveData(value)

inline fun <T> liveDataOf(value: T): LiveData<T> = MutableLiveData(value)

inline operator fun <T> LiveData<T>.getValue(thisObj: Any?, property: KProperty<*>): T? = value

inline operator fun <T> MutableLiveData<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
  this.value = value
}