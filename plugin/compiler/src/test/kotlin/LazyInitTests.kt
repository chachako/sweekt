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
import com.meowool.sweekt.cast
import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

/**
 * @author 凛 (RinOrz)
 */
class LazyInitTests : StringSpec({
  "property marked @JvmField not allowed" {
    compile(
      """
        @LazyInit
        val a: Int = 0

        @LazyInit @JvmField
        var b: Int = 0
      """,
    ) { line ->
      messages.trimEnd().lines().shouldHaveSize(1)
      messages shouldContain "(${line(4)}, 11): " +
        "Property 'b' marked with @LazyInit cannot be marked with @JvmField at the same time."
    }
  }
  "property cannot have getter" {
    compile(
      """
        @LazyInit val a: Int
          get() = 0

        @LazyInit val b: Int get() = 0
      """,
    ) { line ->
      fun error(name: String) = "Property '$name' marked with @LazyInit cannot declare getter (`get() = 0`) at the same time."
      messages.trimEnd().lines().shouldHaveSize(2)
      messages shouldContain "(${line(2)}, 3): ${error("a")}"
      messages shouldContain "(${line(4)}, 22): ${error("b")}"
    }
  }
  "property must have initializer" {
    compile(
      """
        @LazyInit lateinit var a: String

        interface A {
          @LazyInit
          val a: Int
        }
      """,
    ) { line ->
      val error = "Property 'a' marked with @LazyInit must have initializer (`val a = ...`) for the value of lazy initialization."
      messages.trimEnd().lines().shouldHaveSize(2)
      messages shouldContain "(${line(1)}, 24): $error"
      messages shouldContain "(${line(5)}, 7): $error"
    }
  }
  "ensure property 'isInit$?' is generated" {
    compile(
      """
        class A {
          @LazyInit var a: String = ""
          @LazyInit var b: String = ""
          @LazyInit var c: String = ""
        }
      """,
    ) {
      classOf("A").javaClass.declaredFields.map { it.name }.apply {
        shouldHaveSize(6)
        shouldContain("_isInit\$a")
        shouldContain("_isInit\$b")
        shouldContain("_isInit\$c")
      }
    }
  }
  "property is lazy initialization" {
    compile(
      """
        object A {
          @LazyInit val list = listOf("a", "b")
          @LazyInit val block = fun() = true
        }
      """
    ) {
      objectOf("A") {
        field("list").shouldBeNull()
        field("block").shouldBeNull()

        method("getList").shouldBe(listOf("a", "b"))
        method("getBlock").cast<() -> Boolean>().invoke().shouldBe(true)

        field("list").shouldNotBeNull()
        field("block").shouldNotBeNull()
      }
    }
    @Suppress("ConvertToStringTemplate")
    compile(
      """
        import com.meowool.sweekt.firstCharTitlecase
        import io.kotest.matchers.nulls.shouldBeNull
        import io.kotest.matchers.nulls.shouldNotBeNull
        import io.kotest.matchers.shouldBe

        class A {
          @LazyInit var a: Int = 100
          @LazyInit var b: Int = 200
          @LazyInit var c: Int = 300

          fun test(property: Any, expected: Any) = this.javaClass.let {
            property as String
            expected as Int
            val isInit = "_isInit$" + property
            it.getDeclaredField(property).get(this) shouldBe 0
            it.getDeclaredField(isInit).get(this) shouldBe false

            it.getDeclaredMethod("get" + property.firstCharTitlecase()).invoke(this) shouldBe expected

            it.getDeclaredField(property).get(this) shouldBe expected
            it.getDeclaredField(isInit).get(this) shouldBe true
          }
        }
      """,
    ) {
      classOf("A") {
        "test"("a", 100)
        "test"("b", 200)
        "test"("c", 300)
      }
    }
  }
  "only allow reset lazy properties" {
    compile(
      """
        var a: Int = 100
        fun resetA() =
          a.resetLazyValue()
        fun resetInt() = 10.resetLazyValue()
      """
    ) { line ->
      val error = "The extension receiver of the 'resetLazyValue' function must be a property marked with @LazyInit."
      messages shouldContain "(${line(3)}, 3): $error"
      messages shouldContain "(${line(4)}, 18): $error"
    }
    compile("fun reset() = resetLazyValues()") { line ->
      messages shouldContain "(${line(1)}, 15): The called 'resetLazyValues' function requires at least one argument, " +
        "and the argument must be a property marked with @LazyInit."
    }
    compile(
      """
        @LazyInit
        var a: Int = 100
        var b: Int = 100
        fun reset() = resetLazyValues(a, true, b)
      """
    ) { line ->
      val error = "Requires a property marked with @LazyInit as a argument of the 'resetLazyValues' function."
      messages shouldContain "(${line(4)}, 34): $error"
      messages shouldContain "(${line(4)}, 40): $error"
    }
  }
  "test of lazy values reset" {
    compile(
      """
        import io.kotest.matchers.shouldBe
        class A {
          @LazyInit var a: Int = 100
          @LazyInit var b: Int = 200
        }
        class B {
          fun test() {
            A().apply {
              a shouldBe 100
              b shouldBe 200

              a = 0
              b = 0
              a shouldBe 0
              b shouldBe 0

              resetLazyValues(a, b)
              a shouldBe 100
              b shouldBe 200

              a = 0
              a shouldBe 0
              a.resetLazyValue()
              a shouldBe 100
            }
          }
        }
      """,
    ) {
      classOf("B") { method("test") }
    }
  }
  "allow other properties to be called in initializer" {
    compile(
      """
        class A {
          @LazyInit var b: Int = 200
        }
        abstract class AppAppearance {
          abstract val elevation: Int

          @LazyInit
          val windowBorder = mutableListOf(elevation, A().b, 3)

          companion object
        }
      """
    ) { exitCode shouldBe KotlinCompilation.ExitCode.OK }
  }
})
