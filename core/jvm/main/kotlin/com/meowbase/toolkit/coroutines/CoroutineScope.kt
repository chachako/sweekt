@file:Suppress("DeferredIsResult", "unused")

package com.meowbase.toolkit.coroutines

import kotlinx.coroutines.*


/**
 * 使用 [Dispatchers.Main] 调度来启动协程
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 * @see launch
 */
fun CoroutineScope.launchUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Main, start, action)

/**
 * 使用 [Dispatchers.Default] 调度来启动协程
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON等
 * @see launch
 */
fun CoroutineScope.launchDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Default, start, action)

/**
 * 使用 [Dispatchers.IO] 调度来启动协程
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 * @see launch
 */
fun CoroutineScope.launchIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.IO, start, action)


/**
 * 使用 [Dispatchers.Main] 调度来创建一个异步程序
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 * @see async
 */
fun <T> CoroutineScope.asyncUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.Main, start, block)

/**
 * 使用 [Dispatchers.Default] 调度来创建一个异步程序
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON等
 * @see async
 */
fun <T> CoroutineScope.asyncDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.Default, start, block)

/**
 * 使用 [Dispatchers.IO] 调度来创建一个异步程序
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 * @see async
 */
fun <T> CoroutineScope.asyncIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.IO, start, block)
