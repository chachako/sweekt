@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Switch the operation upstream of the stream to the io thread.
 *
 * This dispatcher is optimized to perform disk or network I/O outside of the main thread. Examples
 * include using the Room component, reading from or writing to files, and running any network
 * operations.
 *
 * @see Dispatchers.IO
 */
inline fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)