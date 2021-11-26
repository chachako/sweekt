package com.meowool.sweekt

/**
 * Represents a lazily initialized value property.
 *
 * This annotation is consistent with [kotlin.lazy] behavior, but has better performance:
 * ```
 * @LazyInit
 * var lazyGreeting: String = buildString {
 *   append("Hello, ")
 *   append("World!")
 * }
 *
 * // Lighter than the following code:
 * // val lazyGreeting: String by lazy {
 * //   buildString {
 * //     append("Hello, ")
 * //     append("World!")
 * //   }
 * // }
 * ```
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class LazyInit

/**
 * Resets this lazy property. In other words, the values of this lazy property will be reset to the initial value
 * when the next time is accessed.
 *
 * For example:
 * ```
 * @LazyInit
 * var lazyProp = 1
 * print(lazyProp) // 1
 *
 * lazyProp = 200
 * print(lazyProp) // 200
 *
 * lazyProp.resetLazyValue()
 * print(lazyProp) // 1
 * ```
 *
 * @receiver Property with [LazyInit] annotation.
 * @author 凛 (https://github.com/RinOrz)
 */
@Suppress("unused")
fun Any.resetLazyValue(): Unit = compilerImplementation()

/**
 * Resets all given [lazyProperties]. In other words, these values of lazy properties will be reset to the initial
 * value when the next time is accessed.
 *
 * For example:
 * ```
 * @LazyInit
 * var lazyInt = 1
 * var lazyString = buildString {
 *   append("Hello, ")
 *   append("World!")
 * }
 *
 * print(lazyInt) // 1
 * print(lazyString) // Hello, World!
 *
 * lazyInt = 200
 * print(lazyInt) // 200
 * lazyString = lazyString + " Bye~"
 * print(lazyString) // Hello, World! Bye~
 *
 * resetLazyValue(lazyProp, lazyString)
 * print(lazyInt) // 1
 * print(lazyString) // Hello, World!
 * ```
 *
 * @param lazyProperties Properties with [LazyInit] annotation.
 * @author 凛 (https://github.com/RinOrz)
 */
@Suppress("UNUSED_PARAMETER")
fun resetLazyValues(vararg lazyProperties: Any): Unit = compilerImplementation()