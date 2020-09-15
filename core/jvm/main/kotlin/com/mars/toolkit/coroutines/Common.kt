

package com.mars.toolkit.coroutines

import kotlinx.coroutines.*

/**
 * 使用 [Dispatchers.Main] 调度来启动并挂起一个协程程序
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 * @see withContext
 */
suspend fun <T> withUI(block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.Main, block)

/**
 * 使用 [Dispatchers.Default] 调度来启动并挂起一个协程程序
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON等
 * @see withContext
 */
suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.Default, block)

/**
 * 使用 [Dispatchers.IO] 调度来启动并挂起一个协程程序
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 * @see withContext
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.IO, block)