@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.toolkit.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

inline fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> = MutableLiveData(value)

inline fun <T> liveDataOf(value: T): LiveData<T> = MutableLiveData(value)