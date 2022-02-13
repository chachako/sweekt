@file:Suppress("SpellCheckingInspection", "NOTHING_TO_INLINE", "NAME_SHADOWING")

package com.meowool.sweekt

private const val TypeSeparator = '.'
private const val DescriptorSeparator = '/'
private const val InnerClassSeparator = '$'
private const val DescriptorPrefix = 'L'
private const val ArrayDescriptorPrefix = '['
private const val DescriptorSuffix = ';'
private const val JavaArraySymbol = "[]"

/**
 * E.g.
 * ```
 * Ljava.lang.String; = true
 * Ljava/lang/String; = true
 * [Ljava.lang.String; = false
 * java.lang.String = false
 * Z = false
 * ```
 *
 * @author 凛 (RinOrz)
 */
private fun CharSequence?.isExactJvmTypeDescriptor(): Boolean = this.isNotEmpty()
  && this.first() == DescriptorPrefix
  && this.last() == DescriptorSuffix
  // jvm descriptor only allows `.` and `/` punctuation
  && this.any { it.isPunctuation() && it != DescriptorSeparator && it != TypeSeparator && it != InnerClassSeparator }

/**
 * Returns `true` if this [CharSequence] conforms to the primitive type descriptor or name definition of the JVM.
 *
 * @see CharSequence.isJvmPrimitiveTypeDescriptor
 * @see CharSequence.isJvmPrimitiveTypeName
 *
 * @author 凛 (RinOrz)
 */
inline fun CharSequence?.isJvmPrimitiveType(): Boolean = isJvmPrimitiveTypeDescriptor() || isJvmPrimitiveTypeName()

/**
 * Returns `true` if this [CharSequence] conforms to the primitive type descriptor of the JVM.
 *
 * E.g.
 * ```
 * I = true
 * [I = true
 * [[[[I = true
 * int = false
 * ```
 *
 * @author 凛 (RinOrz)
 */
fun CharSequence?.isJvmPrimitiveTypeDescriptor(): Boolean {
  if (this.isNullOrEmpty()) return false

  // We only need to take an array to check
  val singleArray = substringAfter(lastIndexOf(ArrayDescriptorPrefix) - 1)

  if (this.removeSuffix(singleArray).all { it == ArrayDescriptorPrefix }.not()) return false

  return when (singleArray.size) {
    1 -> singleArray.first().isJvmPrimitiveTypeDescriptor()
    // [B, [Z ...
    2 -> singleArray.first() == ArrayDescriptorPrefix && singleArray.last().isJvmPrimitiveTypeDescriptor()
    else -> false
  }
}

/**
 * Returns `true` if this [Char] conforms to the primitive type descriptor of the JVM.
 *
 * @author 凛 (RinOrz)
 */
fun Char.isJvmPrimitiveTypeDescriptor(): Boolean = when (this) {
  'I', 'Z', 'C', 'D', 'F', 'J', 'S', 'B', 'V' -> true
  else -> false
}

/**
 * Returns `true` if this [CharSequence] conforms to the primitive type name of the JVM.
 *
 * E.g.
 * ```
 * int = true
 * I = false
 * ```
 *
 * @author 凛 (RinOrz)
 */
fun CharSequence?.isJvmPrimitiveTypeName(): Boolean = this.isNotEmpty() && when (this) {
  "int", "boolean", "char", "double", "float", "long", "short", "byte", "void" -> true
  else -> false
}

/**
 * Returns `true` if this [CharSequence] conforms to the array type descriptor of the JVM.
 *
 * E.g.
 * ```
 * [B = true
 * [Ljava.lang.String; = true
 * [[Ljava.lang.String; = true
 * [Ljava/lang/String; = true
 * [Ljava.lang.String = false
 * ```
 *
 * @author 凛 (RinOrz)
 */
fun CharSequence?.isJvmArrayTypeDescriptor(): Boolean {
  if (this.isNullOrEmpty()) return false
  if (this.isJvmPrimitiveTypeDescriptor()) return true

  // We only need to take an array to check
  val singleArray = substringAfter(lastIndexOf(ArrayDescriptorPrefix) - 1)

  if (this.removeSuffix(singleArray).all { it == ArrayDescriptorPrefix }.not()) return false

  return singleArray.removeFirst().isExactJvmTypeDescriptor()
}

/**
 * Returns `true` if this [CharSequence] conforms to the descriptor definition of the JVM.
 * [Reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 *
 * E.g.
 * ```
 * Lcom/a/b; = true
 * Lcom.a.b; = false
 * com/a/b = false
 * com.a.b = false
 * ```
 *
 * @see CharSequence.isJvmPrimitiveTypeDescriptor
 * @author 凛 (RinOrz)
 */
fun CharSequence?.isJvmTypeDescriptor(): Boolean =
  isJvmPrimitiveTypeDescriptor() || isJvmArrayTypeDescriptor() || isExactJvmTypeDescriptor()

/**
 * Converts this [CharSequence] to the descriptor definition of the JVM specification.
 * [Reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 *
 * E.g.
 * ```
 * boolean -> Z
 * java.lang.Object -> Ljava/lang/Object;
 * [I -> [I;
 * [La.b.C; -> [La/b/C;
 * ```
 *
 * @param separator The separator of the type descriptor after conversion
 * @author 凛 (RinOrz)
 */
fun CharSequence.toJvmTypeDescriptor(separator: Char = DescriptorSeparator): String =
  toJvmTypeDescriptor(separator.toString())

/**
 * Converts this [CharSequence] to the descriptor definition of the JVM specification.
 * [Reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 *
 * E.g.
 * ```
 * boolean -> Z
 * java.lang.Object -> Ljava/lang/Object;
 * [I -> [I;
 * [La.b.C; -> [La/b/C;
 * ```
 *
 * @param separator The separator of the type descriptor after conversion.
 *
 * @return Returns the JVM descriptor, if this [CharSequence] is `null` or empty, returns empty string.
 *
 * @author 凛 (RinOrz)
 */
fun CharSequence?.toJvmTypeDescriptor(separator: String): String {
  if (this.isNullOrEmpty()) return ""

  var descriptor = this.toString()
    .replace(TypeSeparator.toString(), separator)
    .replace(DescriptorSeparator.toString(), separator)

  if (descriptor.isJvmTypeDescriptor()) return descriptor

  // Store the possible `[[[` and add it to the beginning of the descriptor when returning
  var arrayDescriptor = ""

  // If it ends with `[]`, we need to change it to `[` at the descriptor beginning
  while (descriptor.endsWith(JavaArraySymbol)) {
    descriptor = descriptor.removeSuffix(JavaArraySymbol)
    arrayDescriptor += ArrayDescriptorPrefix
  }

  fun String.tryWrapArray() = this.takeIf { arrayDescriptor.isEmpty() }
    ?: "$arrayDescriptor$this".takeIf { it.isJvmArrayTypeDescriptor() || it.last() == DescriptorSuffix }
    ?: "$arrayDescriptor$this$DescriptorSuffix"

  if (descriptor.isJvmPrimitiveTypeName()) {
    return when (descriptor) {
      "int" -> "I"
      "boolean" -> "Z"
      "char" -> "C"
      "double" -> "D"
      "float" -> "F"
      "long" -> "J"
      "short" -> "S"
      "byte" -> "B"
      "void" -> "V"
      else -> error("`$descriptor` is a primitive type name definition of JVM, but an unexpected error occurred.")
    }.tryWrapArray()
  }

  descriptor = descriptor.tryWrapArray()

  if (descriptor.last() != DescriptorSuffix) {
    return when {
      // Fix the existence of `[` at the beginning but missing `;` at the end
      descriptor.first() == ArrayDescriptorPrefix -> "$descriptor$DescriptorSuffix"
      // Fix the beginning `L` and the end `;` are missing
      else -> "$DescriptorPrefix$descriptor$DescriptorSuffix"
    }
  }

  return descriptor
}

/**
 * Converts this [CharSequence] to the class name of the JVM qualified.
 *
 * E.g.
 * ```
 * I -> int
 * Ljava/lang/Object; -> java.lang.Object
 * [Ljava/lang/Object; -> [Ljava.lang.Object;
 *
 * // When `canonical` parameter is `true`
 * [La.b.Foo$Bar -> a.b.Foo.Bar[]
 * ```
 *
 * @param canonical If the value is `true`, for example, `[La.b.FooBar` will be canonical as `a.b.Foo.Bar[]`.
 * @param separator The separator of the qualified name after conversion.
 *
 * @return Returns the JVM qualified name, if this [CharSequence] is `null` or empty, returns empty string.
 *
 * @author 凛 (RinOrz)
 */
fun CharSequence?.toJvmQualifiedTypeName(
  canonical: Boolean = false,
  separator: Any = TypeSeparator,
): String {
  if (this.isNullOrEmpty()) return ""

  val separator = separator.toString()

  // Store the possible `[][]` and add it to the end of the qualified name when `canonical` returning
  var arraySymbols = ""

  var qualified = this.toString().replace(DescriptorSeparator.toString(), separator)

  if (qualified.first() == DescriptorPrefix && qualified.last() == DescriptorSuffix) {
    // Only when it starts with `L`, we remove `;`, otherwise it may be a `[Lfoo/bar;` (array descriptor need them)
    qualified = qualified.substring(1, qualified.lastIndex)
  }

  if (canonical) {
    qualified = qualified.removeSuffix(DescriptorSuffix).replace(InnerClassSeparator.toString(), separator)

    // If it starts with `[`, we need to change it to `[]`
    while (qualified.startsWith(ArrayDescriptorPrefix)) {
      qualified = qualified.substring(
        qualified.startsWith("$ArrayDescriptorPrefix$DescriptorPrefix").ifTrue { 2 } ?: 1
      )
      arraySymbols += JavaArraySymbol
    }
  } else if (qualified.first() == DescriptorPrefix && qualified.last() == DescriptorSuffix) {
    // Only when it starts with `L`, we remove `;`, otherwise it may be a `[Lfoo/bar;` (array descriptor need them)
    qualified = qualified.substring(1, qualified.lastIndex)
  }

  return run {
    qualified.isJvmPrimitiveTypeDescriptor().ifTrue {
      when (qualified) {
        "I" -> "int"
        "Z" -> "boolean"
        "C" -> "char"
        "D" -> "double"
        "F" -> "float"
        "J" -> "long"
        "S" -> "short"
        "B" -> "byte"
        "V" -> "void"
        else -> error("`$qualified` is a primitive type desciptor definition of JVM, but an unexpected error occurred.")
      }
    } ?: qualified
  } + arraySymbols
}

/**
 * Converts this [CharSequence] to a simple name definition of the JVM specification.
 *
 * E.g.
 * ```
 * Z -> boolean
 * [I -> int[]
 * java.lang.Object -> Object
 * [La.b.Foo$Bar -> Bar
 * ```
 *
 * @return Returns the JVM package name, if this [CharSequence] is `null` or empty, returns empty string.
 * @author 凛 (RinOrz)
 */
fun CharSequence?.toJvmTypeSimpleName(): String =
  this.toJvmQualifiedTypeName(canonical = true).substringAfterLast(TypeSeparator)

/**
 * Converts this [CharSequence] to a package name definition of the JVM specification.
 * Note that if this [CharSequence] is primitive type will be returns `java.lang`.
 *
 * ```
 * a.b.Foo$Bar -> a.b
 * I -> java.lang
 * ```
 *
 * @return Returns the JVM package name, if this [CharSequence] is `null` or empty, returns empty string.
 * @author 凛 (RinOrz)
 */
fun CharSequence?.toJvmPackageName(): String {
  if (this.isNullOrEmpty()) return ""
  return this.toString()
    .substringBeforeLast(InnerClassSeparator)
    .toJvmQualifiedTypeName(canonical = true)
    .replace(JavaArraySymbol, "").run {
      this.isJvmPrimitiveType().ifTrue { "java.lang" } ?: this.toJvmQualifiedTypeName().substringBeforeLast(TypeSeparator)
    }
}
