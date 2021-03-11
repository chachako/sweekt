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

@file:Suppress("DeferredIsResult")

package com.meowbase.toolkit.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowbase.toolkit.coroutines.asyncUI
import com.meowbase.toolkit.coroutines.asyncDefault
import com.meowbase.toolkit.coroutines.asyncIO
import com.meowbase.toolkit.coroutines.launchDefault
import com.meowbase.toolkit.coroutines.launchIO
import com.meowbase.toolkit.coroutines.launchUI
import kotlinx.coroutines.*

/**
 * 在 [ViewModel] 中使用 [Dispatchers.Main] 调度来启动协程
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 *
 * @see CoroutineScope.launchUI
 * @see launch
 */
fun ViewModel.launchUI(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchUI(start, action)

/**
 * 在 [ViewModel] 中使用 [Dispatchers.Default] 调度来启动协程
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON等
 *
 * @see CoroutineScope.launchDefault
 * @see launch
 */
fun ViewModel.launchDefault(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchDefault(start, action)

/**
 * 在 [ViewModel] 中使用 [Dispatchers.IO] 调度来启动协程
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 *
 * @see CoroutineScope.launchIO
 * @see launch
 */
fun ViewModel.launchIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchIO(start, action)


/**
 * 在 [ViewModel] 中使用 [Dispatchers.Main] 调度来创建一个异步程序
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 *
 * @see CoroutineScope.asyncUI
 * @see async
 */
fun <T> ViewModel.asyncUI(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncUI(start, block)

/**
 * 在 [ViewModel] 中使用 [Dispatchers.Default] 调度来创建一个异步程序
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON等
 *
 * @see CoroutineScope.asyncDefault
 * @see async
 */
fun <T> ViewModel.asyncDefault(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncDefault(start, block)

/**
 * 在 [ViewModel] 中使用 [Dispatchers.IO] 调度来创建一个异步程序
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 *
 * @see CoroutineScope.asyncIO
 * @see async
 */
fun <T> ViewModel.asyncIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncIO(start, block)
