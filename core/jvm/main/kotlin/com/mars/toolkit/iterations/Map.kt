@file:Suppress("FunctionName")

package com.mars.toolkit.iterations

/**
 * 转换 Map<K, V> 为人类可读的字符串
 * ```
 * mapOf("one" to true, "two" to 2).joinToString(StringBuilder(infix = "="))
 * // { one=true, two=2 }
 * ```
 */
fun <K, V, A : Appendable> Map<K, V>.joinTo(
  buffer: A,
  infix: CharSequence = ": ",
  separator: CharSequence = ", ",
  prefix: CharSequence = "{ ",
  postfix: CharSequence = " }",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): A {
  buffer.append(prefix)
  var count = 0
  for (element in this) {
    if (++count > 1) buffer.append(separator)
    if (limit < 0 || count <= limit) {
      buffer._appendKV(infix, element.key, element.value, transform)
    } else break
  }
  if (limit in 0 until count) buffer.append(truncated)
  buffer.append(postfix)
  return buffer
}

/**
 * 转换 Map<K, V> 为人类可读的字符串
 * ```
 * mapOf("one" to true, "two" to 2).joinToString()
 * // { one: true, two: 2 }
 * ```
 */
fun <K, V> Map<K, V>.joinToString(
  separator: CharSequence = ", ",
  infix: CharSequence = ": ",
  prefix: CharSequence = "{ ",
  postfix: CharSequence = " }",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): String = joinTo(
  buffer = StringBuilder(),
  infix = infix,
  separator = separator,
  prefix = prefix,
  postfix = postfix,
  limit = limit,
  truncated = truncated,
  transform = transform
).toString()

private fun <K, V> Appendable._appendKV(
  infix: CharSequence,
  key: K,
  value: V,
  transform: ((K, V) -> CharSequence)?
) {
  fun Any?.get(): CharSequence? = when (this) {
    is CharSequence? -> this
    else -> this.toString()
  }
  when {
    transform != null -> append(transform(key, value))
    else -> append("${key.get()}$infix${value.get()}")
  }
}