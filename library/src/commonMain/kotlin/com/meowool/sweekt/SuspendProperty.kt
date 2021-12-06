@file:Suppress("FunctionName")

package com.meowool.sweekt

/**
 * Represents that the getter of the property is suspended.
 *
 * This means that the marked property can only be got in suspend context:
 * ```
 * @SuspendGetter
 * var greeting: String
 *   get() = suspendGetter { ... }
 *   set(value) = suspendSetter { ... }
 *
 * fun a() {
 *   greeting = "Hello world"
 *   ^^^^^^^^ ERROR: Property is only allowed to be changed value in the suspend context.
 *
 *   print(greeting)
 *         ^^^^^^^^ ERROR: Property is only allowed to be got in the suspend context.
 * }
 *
 * suspend fun b() {
 *   print(greeting) // OK
 *   greeting = "Hello world" // OK
 * }
 * ```
 *
 * @see suspendGetter
 * @see suspendSetter
 * @author 凛 (https://github.com/RinOrz)
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Suspend

/**
 * Runs a 'suspend' getter block and returns the result.
 *
 * Note that this will give the property getter the same behavior as the suspend function:
 * ```
 * val value: Int
 *   get() = suspendGetter {
 *     withContext(Dispatchers.IO) {
 *       ...
 *     }
 *   }
 *
 * ----------------- equals -----------------
 *
 * suspend fun value() {
 *   withContext(Dispatchers.IO) {
 *     ...
 *   }
 * }
 * ```
 *
 * @see Suspend
 * @author 凛 (https://github.com/RinOrz)
 */
@Suppress("UNUSED_PARAMETER")
fun <R> suspendGetter(block: suspend () -> R): R = compilerImplementation()

/**
 * Runs a 'suspend' setter block.
 *
 * Note that this will give the property setter the same behavior as the suspend function:
 * ```
 * var value: Int = 0
 *   set(value) = suspendSetter {
 *     withContext(Dispatchers.IO) {
 *       field = 100
 *     }
 *   }
 *
 * print(value) // 100
 *
 * ----------------- equals -----------------
 *
 * var value: Int = 0
 * suspend fun setValue(value: Int) {
 *   withContext(Dispatchers.IO) {
 *     value = 100
 *   }
 * }
 *
 * print(setValue(100)) // 100
 * ```
 *
 * @see Suspend
 * @author 凛 (https://github.com/RinOrz)
 */
@Suppress("UNUSED_PARAMETER")
inline fun suspendSetter(crossinline block: suspend () -> Unit): Unit = compilerImplementation()


/////////////////////////////////////////////////////////////////////////////
////                            Internal APIs                            ////
/////////////////////////////////////////////////////////////////////////////

@InternalSweektCompilerApi
@Deprecated("@InternalSweektCompilerApi", level = DeprecationLevel.HIDDEN)
suspend inline fun <R> `-$suspendGetter`(crossinline block: suspend () -> R): R = block()

@InternalSweektCompilerApi
@Deprecated("@InternalSweektCompilerApi", level = DeprecationLevel.HIDDEN)
suspend inline fun `-$suspendSetter`(crossinline block: suspend () -> Unit): Unit = block()