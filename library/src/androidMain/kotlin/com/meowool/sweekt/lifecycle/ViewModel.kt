@file:Suppress("DeferredIsResult")

package com.meowool.sweekt.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowool.sweekt.coroutines.asyncDefault
import com.meowool.sweekt.coroutines.asyncIO
import com.meowool.sweekt.coroutines.asyncUI
import com.meowool.sweekt.coroutines.launchDefault
import com.meowool.sweekt.coroutines.launchIO
import com.meowool.sweekt.coroutines.launchUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.launch].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ViewModel.launch(
  context: CoroutineContext = EmptyCoroutineContext,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, action)

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.launchUI].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ViewModel.launchUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchUI(start, action)

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.launchDefault].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ViewModel.launchDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchDefault(start, action)

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.launchIO].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ViewModel.launchIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = viewModelScope.launchIO(start, action)


/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.asyncUI].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> ViewModel.asyncUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncUI(start, block)

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.asyncDefault].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> ViewModel.asyncDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncDefault(start, block)

/**
 * Launch a coroutine in the [ViewModel.viewModelScope].
 *
 * For more details [CoroutineScope.asyncIO].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> ViewModel.asyncIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = viewModelScope.asyncIO(start, block)