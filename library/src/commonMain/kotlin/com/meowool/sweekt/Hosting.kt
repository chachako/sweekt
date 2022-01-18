@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "UNCHECKED_CAST", "NO_ACTUAL_FOR_EXPECT", "DEPRECATION")

package com.meowool.sweekt

import kotlin.reflect.KProperty

/**
 * Represents a [value] hosting class.
 *
 * @author 凛 (RinOrz)
 */
@Deprecated("Use `@LazyInit` instead.")
interface Hosting<T> {

  /**
   * Get or host the value.
   *
   * Note that if not any value is hosted and get it, should throw an error.
   */
  var value: T

  /**
   * Returns the hosted value. If not any value is hosted in this hosting, calls the [defaultValue]
   * function, puts its result into the hosting and returns it.
   *
   * @see value
   */
  fun getOrHost(defaultValue: () -> T): T

  /**
   * Returns the hosted value safely. If not any value is hosted in this hosting, returns
   * the `null`, avoid calling [value] directly to cause an exception.
   *
   * @see value
   */
  fun getOrNull(): T?

  /**
   * Returns `true` if any value is hosted.
   */
  fun isHosting(): Boolean

  /**
   * Invalidate this hosting, this will clear the value currently being hosted.
   */
  fun invalidate()

  /**
   * An extension allows using instances of [Hosting] for mutable property delegation.
   *
   * E.g. `var value: String by hosting { initializer }`
   */
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

  /**
   * An extension allows using instances of [Hosting] for immutable property delegation.
   *
   * E.g. `val value: String by hosting { initializer }`
   */
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T
}

/**
 * Record all hosting instances so that they can be taken away at any times.
 *
 * Note that this stack is only used to access or store hosting objects, not the [Hosting.value].
 *
 * @author 凛 (RinOrz)
 */
@Deprecated("Use `@LazyInit` instead.")
object HostingStack {
  private val instances = mutableMapOf<Any, Hosting<*>>()

  /**
   * Record the new [instance] with given [key].
   */
  fun record(key: Any, instance: Hosting<*>) {
    require(instances.containsKey(key).not()) {
      "A `Hosting` instance with the same key `$key` already exists, please change to another key, " +
        "or use `HostingStack.remove(\"$key\")` function to remove the existing instance first."
    }
    instances[key] = instance
  }

  /**
   * Returns the instance corresponding to given [key] if has been recorded, otherwise return null.
   */
  fun find(key: Any): Hosting<*>? = instances[key]

  /**
   * Returns the instance corresponding to given [key].
   */
  operator fun get(key: Any): Hosting<*> = find(key)
    ?: error("There is no `Hosting` instance of key `$key`, please make sure you created the instance with this key earlier.")

  /**
   * Returns the instance corresponding to given [key].
   */
  fun <T> take(key: Any): Hosting<T> = get(key) as Hosting<T>

  /**
   * Removes the instance corresponding to given [key] in this stack.
   */
  @Deprecated("Use `resetLazyValue(lazyValue)` instead.", ReplaceWith("resetLazyValue(...)"))
  fun remove(key: Any) = instances.remove(key)

  /**
   * Removes the instance corresponding to given [key] in this stack.
   */
  @Deprecated("Use `resetLazyValue(lazyProperty)` instead.", ReplaceWith("resetLazyValue(...)"))
  operator fun minus(key: Any) = instances.remove(key)
}

/**
 * Creates an empty [Hosting] to host the value at some point in the future.
 *
 * @author 凛 (RinOrz)
 */
@Deprecated("Use `@LazyInit var foo = ???` instead.", ReplaceWith("@LazyInit var ? = ???"))
fun <T> hosting(key: Any? = null): Hosting<T> = HostingImpl<T>().also { instance ->
  key?.let { HostingStack.record(it, instance) }
}

/**
 * Create a lazy [Hosting] instance.
 *
 * Only when the hosted value is called, and it does not exist, will create a new value uses the
 * specified [initializer] and hosted it.
 *
 * This allows hosting to have the same behavior as [Lazy], please see [kotlin.lazy] for [lock]
 * and more details.
 *
 * @param key if the key exists, the hosting instance will be recorded in the stack to find them at any time.
 * @author 凛 (RinOrz)
 */
@Deprecated("Use `@LazyInit var foo = ???` instead.", ReplaceWith("@LazyInit var ? = ???"))
fun <T> hosting(
  key: Any? = null,
  lock: Any? = null,
  initializer: () -> T,
): Hosting<T> = LazyHostingImpl(lock, initializer).also { instance ->
  key?.let { HostingStack.record(it, instance) }
}


/////////////////////////////////////////////////
////                 Backend                 ////
/////////////////////////////////////////////////

internal expect class HostingImpl<T>() : Hosting<T>
internal expect class LazyHostingImpl<T>(lock: Any?, initializer: () -> T) : Hosting<T>