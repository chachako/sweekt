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

package com.meowbase.toolkit.os

import android.os.Looper


val mainLooperOrNull: Looper? get() = Looper.getMainLooper()
val mainLooper: Looper get() = mainLooperOrNull!!

val mainThread: Thread = mainLooper.thread

inline val isMainThread get() = mainThread === Thread.currentThread()

inline val isMainLooper get() = Looper.myLooper() == Looper.getMainLooper()

/** 确保调用的方法必须要在主线程上 */
fun requireMainThread() = require(isMainThread) {
  "不允许在非主线程上调用方法，当前线程为: ${Thread.currentThread()}"
}

/** 确保调用的方法不能在主线程上 */
fun requireNotMainThread() = require(!isMainThread) {
  "只能在主线程上调用方法! 当前线程为: ${Thread.currentThread()}"
}