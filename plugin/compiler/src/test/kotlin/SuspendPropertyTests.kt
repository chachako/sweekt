import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class SuspendPropertyTests : StringSpec({
  "correct result of suspend call" {
    compile(
      """
        import com.meowool.sweekt.*
        import kotlinx.coroutines.*
        import io.kotest.matchers.shouldBe

        interface A {
          @Suspend var name: String
        }

        class AImpl : A {
          @Suspend override var name: String = "A."
            get() = suspendGetter { field + fetchName() }
            set(value) = suspendSetter { field = value + fetchName() }

          private suspend fun fetchName(): String = "ABC"

          private fun main() = runBlocking { name }
        }

        object Test {
          fun compare() = runBlocking { 
            val instance: A = AImpl()
            instance.name shouldBe AImpl().name
          }
        }
      """.trimIndent()
    ) {
      classOf("AImpl").method("main") shouldBe "A.ABC"
      objectOf("Test").method("compare")
      exitCode shouldBe KotlinCompilation.ExitCode.OK
    }
  }
  "need to be called in context" {
    compile(
      """
        import kotlinx.coroutines.*

        @Suspend val id: Int
          get() = suspendGetter { 0 }

        fun main() {
          val result = runBlocking { id }
          println(id)
        }
      """.trimIndent()
    ) { line ->
      messages shouldContain "(${line(8)}, 11): " +
        "Suspend property 'id' should be accessed only from a coroutine or suspend function"
    }
  }
  "accessors required 'suspendGetter' / 'suspendSetter'" {
    compile(
      """
        @Suspend val immutable: Int
          get() = 0

        @Suspend var mutable: Int = 100
          @Suppress("RedundantSetter") set(value) {
            field = value
          }
      """.trimIndent()
    ) { line ->
      messages shouldContain "(${line(2)}, 3): @Suspend Property 'immutable' getter must return `suspendGetter`."
      messages shouldContain "(${line(5)}, 3): @Suspend Property 'mutable' setter must be call `suspendSetter`."
    }
    compile(
      """
        class A {
          @Suspend val immutable: Int
            get() = suspendGetter { 0 }

          @Suspend var mutable: Int = 100
            set(value) = suspendSetter { field = value }

          @Suspend val immutable1: Int
            get() {
              println("abc")
              return suspendGetter { 0 }
            }

          @Suspend var mutable1: Int = 100
            set(value) {
              println("start")
              suspendSetter { field = value }
              println("end")
            }
        }
      """.trimIndent()
    ) {
      exitCode shouldBe KotlinCompilation.ExitCode.OK
    }
  }
})