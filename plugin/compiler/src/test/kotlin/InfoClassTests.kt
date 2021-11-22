/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("MemberVisibilityCanBePrivate")

import com.meowool.sweekt.SweektNames.Root
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class InfoClassTests : StringSpec({
  "constructor parameter cannot have the same name as the defined property" {
    compile(
      """
        @Info(generateCopy = true)
        class Foo(a: Int) {
          val a = a
        }
        @Info
        class Bar(a: Int) {
          val a = a
        }
      """
    ) { line ->
      messages shouldContain "(${line(7)}, 7): Property 'a' name conflicts with the parameter in the constructor. " +
        "This is not allowed in the class marked with `@Info(generateCopy = true)`"
    }
  }
  "must have a primary constructor when multi-constructors are declared" {
    compile(
      """
        @Info
        class Foo {
          constructor()
          constructor(@Suppress("UNUSED_PARAMETER") bar: Int)
        }

        @Info
        class Foo(val baz: String) {
          constructor() : this("")
          constructor(bar: Int) : this(bar.toString())
        }
      """
    ) { line ->
      val error = "Class marked with @Info declares multiple constructors, " +
        "please declare the primary constructor explicitly, or change the marker to: `@Info(generateCopy = false)`."
      messages shouldContain "(${line(3)}, 3): $error"
      messages shouldNotContain "(${line(9)}, 3): $error"
    }
  }
  "multi-constructors are allowed declared when `copy` is not generated" {
    compile(
      """
        @Info(generateToString = true, generateCopy = false)
        class Foo {
          constructor()
          constructor(@Suppress("UNUSED_PARAMETER") bar: Int)
        }

        @Info(false)
        class Bar {
          constructor()
          constructor(@Suppress("UNUSED_PARAMETER") bar: Int)
        }
      """
    ) {
      messages shouldNotContain "Class marked with '@Info' declares multiple constructors, " +
        "please declare the primary constructor explicitly, or change the marker to: `@Info(generateCopy = false)`."
    }
  }
  "unsupported special class" {
    compile(
      """
        @Info enum class Foo { A }
        @Info annotation class Foo
        @Info interface Foo
      """
    ) { line ->
      fun error(kind: String) = "It is not allowed to mark @Info on the $kind class."
      messages shouldContain "(${line(1)}, 7): " + error("enum")
      messages shouldContain "(${line(2)}, 7): " + error("annotation")
      messages shouldContain "(${line(3)}, 17): " + error("interface")
    }
  }
  "call super function" {
    compile(
      """
        open class Base {
          override fun hashCode(): Int = 100
          override fun equals(other: Any?): Boolean = false
        }

        @Info
        class Foo : Base() {
          val a = 10

          fun testHashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + a.hashCode()
            return result
          }

          fun testEquals(other: Any?): Boolean {
            if (!super.equals(other)) return false
            return testNoSuperEquals(other)
          }

          fun testNoSuperEquals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Foo) return false
            if (a != other.a) return false
            return true
          }
        }
      """
    ) {
      messages shouldContain "super call '$Root.Base.hashCode'."
      classOf("Foo") {
        "testHashCode"() shouldBe hashCode()
        "testEquals"(instance) shouldBe equals(instance).apply { shouldBeFalse() }
        "testNoSuperEquals"(instance) shouldBe true
      }
    }
  }
  "collect properties that need to be generated" {
    compile(
      """
        @Info
        class Foo(
          param: String = "foo",
          val primary: Boolean = true,
          @Info.Invisible val invisible: Boolean = true,
        ) {
          val int: Int = 100
          private val hiddenInt: Int = 0
        }
      """
    ) {
      val common = "primary, int"
      messages shouldContain "generateEquals for [$common]"
      messages shouldContain "generateHashCode for [$common]"
      messages shouldContain "generateToString for [$common]"
      messages shouldContain "generateCopy for [param, $common]"
      messages shouldContain "generateComponentN for [$common]"
    }
  }
  "objects is equals" {
    compile(
      """
        @Info
        class Foo {
          val primary: Boolean = true
          val array: Array<String> = emptyArray()
          val longArray: LongArray = LongArray(0)
          val list: List<String> = listOf("a", "b", "c")
          val nullable: Map<Int, String>? = null
          val uIntArray: UIntArray = uintArrayOf(1u, 2u, 3u)
          val uInt: UInt = 80u
          val int: Int = 100

          fun testEquals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Foo) return false

            if (primary != other.primary) return false
            if (!array.contentEquals(other.array)) return false
            if (!longArray.contentEquals(other.longArray)) return false
            if (list != other.list) return false
            if (nullable != other.nullable) return false
            if (uIntArray != other.uIntArray) return false
            if (uInt != other.uInt) return false
            if (int != other.int) return false

            return true
          }
        }
      """
    ) {
      classOf("Foo") {
        "testEquals"(instance) shouldBe true
        "testEquals"(null) shouldBe false
      }
    }
  }
  "hashCode is same when the objects is equals" {
    compile(
      """
        @Info
        class Foo {
          val primary: Boolean = true
          val array: Array<String> = arrayOf("10", "20", "30", "40", "50")
          val longArray: LongArray = LongArray(0)
          val list: List<String> = listOf("a", "b", "c")
          val nullable: Map<Int, String>? = null
          val uIntArray: UIntArray = uintArrayOf(1u, 2u, 3u)
          val uInt: UInt = 80u
          val int: Int = 100

          fun testHashCode(): Int {
            var result = primary.hashCode()
            result = 31 * result + array.contentHashCode()
            result = 31 * result + longArray.contentHashCode()
            result = 31 * result + list.hashCode()
            result = 31 * result + (nullable?.hashCode() ?: 0)
            result = 31 * result + uIntArray.hashCode()
            result = 31 * result + uInt.hashCode()
            result = 31 * result + int.hashCode()
            return result
          }
        }
      """
    ) {
      classOf("Foo") {
        "testHashCode"() shouldBe hashCode()
      }
    }
  }
  "generate the correct 'toString'" {
    compile(
      """
        @Info
        class Foo {
          val boolean: Boolean = true
          val array: Array<String> = arrayOf("a", "b", "c")
          val longArray: LongArray = LongArray(0)
          val list: List<String> = listOf("A", "B", "C")
          val map: Map<Int, String> = mutableMapOf(1 to "a", 2 to "b", 3 to "c")
          val nullable: Any? = null
        }
      """
    ) {
      classOf("Foo") {
        toString() shouldBe "Foo(boolean=true, array=[a, b, c], longArray=[], list=[A, B, C], map={1=a, 2=b, 3=c}, nullable=null)"
      }
    }
    compile(
      """
        @Info
        class Foo {}
      """
    ) {
      classOf("Foo") {
        toString() shouldBe "Foo()"
      }
    }
  }
  "manually defined methods will not be regenerated" {
    compile(
      """
        @Info
        class Foo(param: Any) {
          override fun hashCode(): Int = 100
          fun copy(param: Any): Foo = Foo(100)
        }
      """
    ) {
      classOf("Foo", 0) {
        "hashCode"() shouldBe 100
        "copy"(0) shouldNotBe instance
      }
    }
  }
  "manual override of info synthetic functions is not allowed" {
    compile(
      """
        @Info
        class Foo {
          val boolean: Boolean = true
          val int = 100
          override fun equals(other: Any?): Boolean = infoEquals(other)
          override fun toString(): String = "Synthetic > " + infoToString()
        }
      """
    )
  }
  "test info synthetic functions" {
    compile(
      """
        @Info
        class Foo {
          val boolean: Boolean = true
          val int = 100
          override fun equals(other: Any?): Boolean = infoEquals(other)
          override fun toString(): String = "Synthetic > " + infoToString()
        }
      """
    ) {
      classOf("Foo") {
        toString() shouldBe "Synthetic > Foo(boolean=true, int=100)"
        "infoEquals"(instance) shouldBe true
        equals(instance) shouldBe true
      }
    }
  }
  "test synthetic of copy" {
    compile(
      """
        import io.kotest.matchers.shouldBe

        @Info
        class Foo(val a: Boolean, b: Int) {
          val c: String = "50"
        }

        fun main() {
          Foo(false, 2).copy(a = true, b = 0, c = "next").apply {
            a shouldBe true
            b shouldBe 0
            c shouldBe "next"
          }
          Foo(false, 2).copy(a = true, c = "next")
          Foo(false, 2).copy(true)
        }
      """
    ) { line ->
      messages shouldContain "(${line(14)}, 42): No value passed for parameter 'b'"
      messages shouldContain "(${line(15)}, 22): The boolean literal does not conform to the expected type Int"
    }
  }
  "test synthetic of componentN" {
    compile(
      """
        import io.kotest.matchers.shouldBe
        @Info
        class Foo(val a: Int, b: Int) {
          val c: Int = 50
        }

        @Info(joinPrivateProperties = true)
        class Bar(
          @Info.Invisible private val a: Int,
          @Info.Invisible(generateComponentN = true) private val b: Boolean
        )

        @Info
        class Baz(
          private val a: Int,
          private val b: Boolean
        )

        fun main() {
          Foo(1, 2).component1() shouldBe 1
          Foo(1, 2).component2() shouldBe 50
          Bar(100, true).component1() shouldBe true
          val (a, b) = Baz(100, true)
        }
      """
    ) { line ->
      messages shouldContain "(${line(23)}, 16): " +
        "Destructuring declaration initializer of type Baz must have a 'component1()' function"
    }
  }
})
