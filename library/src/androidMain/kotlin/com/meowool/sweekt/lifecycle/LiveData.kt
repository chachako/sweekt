@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

inline fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> = MutableLiveData(value)

inline fun <T> liveDataOf(value: T): LiveData<T> = MutableLiveData(value)

inline operator fun <T> LiveData<T>.getValue(thisObj: Any?, property: KProperty<*>): T? = value

inline operator fun <T> MutableLiveData<T>.setValue(
  thisObj: Any?,
  property: KProperty<*>,
  value: T
) {
  this.value = value
}