package kotlin

/** FIXME: 什么时候 Gradle 内置了 1.4版本的 kotlin 了才能删 */
@PublishedApi
internal class KotlinNothingValueException : RuntimeException {
  constructor() : super()
  constructor(message: String?) : super(message)
  constructor(message: String?, cause: Throwable?) : super(message, cause)
  constructor(cause: Throwable?) : super(cause)
}