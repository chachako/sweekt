@file:Suppress("SpellCheckingInspection")

import com.meowool.sweekt.isJvmArrayTypeDescriptor
import com.meowool.sweekt.isJvmTypeDescriptor
import com.meowool.sweekt.isJvmPrimitiveType
import com.meowool.sweekt.isJvmPrimitiveTypeDescriptor
import com.meowool.sweekt.toJvmPackageName
import com.meowool.sweekt.toJvmQualifiedTypeName
import com.meowool.sweekt.toJvmTypeDescriptor
import com.meowool.sweekt.toJvmTypeSimpleName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class JvmDescriptorTests : StringSpec({
  "isJvmPrimitiveType" {
    "Z".isJvmPrimitiveType() shouldBe true
    "A".isJvmPrimitiveType() shouldBe false
    "boolean".isJvmPrimitiveType() shouldBe true
    "char".isJvmPrimitiveType() shouldBe true
  }
  "isJvmPrimitiveTypeDescriptor" {
    "B".isJvmPrimitiveTypeDescriptor() shouldBe true
    'V'.isJvmPrimitiveTypeDescriptor() shouldBe true
    "boolean".isJvmPrimitiveTypeDescriptor() shouldBe false
  }
  "isJvmArrayTypeDescriptor" {
    "[Z".isJvmArrayTypeDescriptor() shouldBe true
    "[[[J".isJvmArrayTypeDescriptor() shouldBe true
    "[[Ljava.lang.String;".isJvmArrayTypeDescriptor() shouldBe true
    "[Ljava/lang/String;".isJvmArrayTypeDescriptor() shouldBe true
    "[Ljava.lang.String".isJvmArrayTypeDescriptor() shouldBe false
  }
  "isJvmDescriptor" {
    "Z".isJvmTypeDescriptor() shouldBe true
    "[[[I".isJvmTypeDescriptor() shouldBe true
    "Lcom/a/b;".isJvmTypeDescriptor() shouldBe true
    "Lcom.a.b;".isJvmTypeDescriptor() shouldBe true
    "com/a/b".isJvmTypeDescriptor() shouldBe false
    "com.a.b".isJvmTypeDescriptor() shouldBe false
    "[com.a.b".isJvmTypeDescriptor() shouldBe false
    "[com/a/b".isJvmTypeDescriptor() shouldBe false
    "[com/a/b;".isJvmTypeDescriptor() shouldBe false
    "[Lcom/a/b;".isJvmTypeDescriptor() shouldBe true
  }

  "toJvmTypeDescriptor" {
    "boolean".toJvmTypeDescriptor() shouldBe "Z"
    "java.lang.Object".toJvmTypeDescriptor() shouldBe "Ljava/lang/Object;"
    "java.lang.Object\$Bar".toJvmTypeDescriptor() shouldBe "Ljava/lang/Object\$Bar;"
    "boolean[]".toJvmTypeDescriptor() shouldBe "[Z"
    "[Z".toJvmTypeDescriptor() shouldBe "[Z"
    "[La.b.C;".toJvmTypeDescriptor() shouldBe "[La/b/C;"
    "[[[I".toJvmTypeDescriptor() shouldBe "[[[I"
    "[[[i".toJvmTypeDescriptor() shouldBe "[[[i;"
  }

  "toJvmQualifiedTypeName" {
    "I".toJvmQualifiedTypeName() shouldBe "int"
    "Ljava/lang/Object;".toJvmQualifiedTypeName() shouldBe "java.lang.Object"
    "Lcom.a.Foo\$Bar;".toJvmQualifiedTypeName() shouldBe "com.a.Foo\$Bar"
    "[Ljava/lang/Object;".toJvmQualifiedTypeName() shouldBe "[Ljava.lang.Object;"
    "[La.b.Foo\$Bar".toJvmQualifiedTypeName(canonical = true) shouldBe "a.b.Foo.Bar[]"
  }

  "toJvmTypeSimpleName" {
    "Z".toJvmTypeSimpleName() shouldBe "boolean"
    "[I".toJvmTypeSimpleName() shouldBe "int[]"
    "java.lang.Object".toJvmTypeSimpleName() shouldBe "Object"
    "La.b.Foo\$Bar".toJvmTypeSimpleName() shouldBe "Bar"
  }

  "toJvmPackageName" {
    "Z".toJvmPackageName() shouldBe "java.lang"
    "[I".toJvmPackageName() shouldBe "java.lang"
    "a.b.Foo\$Bar".toJvmPackageName() shouldBe "a.b"
    "[Ljava/lang/Object;".toJvmPackageName() shouldBe "java.lang"
  }
})