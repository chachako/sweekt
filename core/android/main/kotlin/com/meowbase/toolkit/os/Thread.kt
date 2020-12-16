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