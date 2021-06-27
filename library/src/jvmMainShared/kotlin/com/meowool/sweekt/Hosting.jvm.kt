@file:Suppress("UNCHECKED_CAST", "PropertyName", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.reflect.KProperty

private object InvalidHosting

private const val ErrorHosting = "You have not hosted any value!"

internal actual open class HostingImpl<T> : Hosting<T> {
  @Volatile internal var _value: Any? = InvalidHosting

  override var value: T
    get() = when (isHosting()) {
      true -> _value as T
      else -> error(ErrorHosting)
    }
    set(value) {
      _value = value
    }

  final override inline fun getOrHost(defaultValue: () -> T): T {
    if (isHosting().not()) {
      _value = defaultValue()
    }
    return value
  }

  final override inline fun isHosting(): Boolean = _value !== InvalidHosting

  final override inline fun invalidate() {
    _value = InvalidHosting
  }

  final override inline fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    _value = value
  }

  final override inline fun getValue(thisRef: Any?, property: KProperty<*>): T = value

  override fun getOrNull(): T? = if (isHosting()) value else null

  override fun toString(): String = value.toString()
}

internal actual class LazyHostingImpl<T> actual constructor(
  lock: Any?,
  private var initializer: (() -> T)?
) : HostingImpl<T>(), Hosting<T> {
  // final field is required to enable safe publication of constructed instance
  private val lock = lock ?: this

  override var value: T
    set(value) {
      _value = value
    }
    get() = when (isHosting()) {
      true -> _value as T
      else -> synchronized(lock) {
        when (isHosting()) {
          true -> _value as T
          else -> {
            // is is not empty when it is first getting
            val typedValue = initializer?.invoke() ?: error(ErrorHosting)
            _value = typedValue
            initializer = null
            typedValue
          }
        }
      }
    }
}