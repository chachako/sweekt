package com.mars.toolkit


const val NoGetter = "这个属性不允许被获取，你只能够对其进行赋值"

/**
 * Usage example:
 * `@Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter`
 */
inline val noGetter: Nothing
  get() = throw UnsupportedOperationException(NoGetter)

/**
 * Run block
 * Try catch don't exist class
 */
inline fun runAndIgnoreClass(func: () -> Unit) = try {
  func()
} catch (_: ClassNotFoundException) {
} catch (_: IllegalStateException) {
} catch (_: NoSuchMethodError) {
} catch (_: Error) {
}

/**
 * Conveniently try catch api
 * ```
 * trying {
 *   // run code
 * } catch {
 *   // catch any errors
 * }
 * ```
 */
typealias TryBlock = () -> Unit

fun trying(block: TryBlock) = block

inline infix fun TryBlock.catch(error: (Throwable) -> Unit) {
  try {
    this()
  } catch (t: Throwable) {
    error(t)
  }
}

/**
 * Security run block
 * print stack when error
 */
inline fun <T : Any> tryVerbosely(func: () -> T?) = try {
  func()
} catch (t: Throwable) {
  t.printStackTrace()
}

/**
 * Run block by quietly
 * And [tryVerbosely] the same, but not print stack
 */
inline fun <T : Any> tryQuietly(func: () -> T?) = try {
  func()
} catch (t: Throwable) {
}

/** Throws an [IllegalArgumentException] with the given [message] */
fun illegalArg(message: Any): Nothing = throw IllegalArgumentException(message.toString())

/** Throws an [UnsupportedOperationException] with the given [message] */
fun unsupported(message: Any): Nothing = throw UnsupportedOperationException(message.toString())

/** Throws an [NullPointerException] with the given [message] */
fun nullPointer(message: Any): Nothing = throw NullPointerException(message.toString())

/** Throws an [NoSuchElementException] with the given [message] */
fun noSuchElement(message: String): Nothing = throw NoSuchElementException(message)