import com.meowool.sweekt.HostingStack
import com.meowool.sweekt.hosting
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

/**
 * @author 凛 (RinOrz)
 */
class HostingTests : BehaviorSpec({
  val host = hosting<Int>("one")

  given("empty hosting") {
    `when`("entry") {
      host.isHosting() shouldBe false
    }
    then("put the value") {
      host.value = 0
      host.value shouldBe 0
      host.isHosting() shouldBe true
    }
    then("change the value") {
      host.value = 1
      host.value shouldBeGreaterThan 0
    }
  }

  given("not empty hosting") {
    `when`("get value") {
      host.getOrHost { -1 } shouldBe 1
    }
    `when`("invalidate hosting") {
      host.invalidate()
      host.isHosting() shouldBe false
    }
    `when`("get or hosting new value") {
      host.getOrHost { -1 } shouldBe -1
      host.isHosting() shouldBe true
      host.value shouldBe -1
    }
    then("invalidate hosting") {
      HostingStack["one"].invalidate()
      shouldThrow<IllegalStateException> { host.value }.message shouldBe "You have not hosted any value!"
    }
  }

  val delegatedHost by hosting("two") { listOf(1, 2, 3) }
  given("hosting property") {
    `when`("entry") {
      delegatedHost shouldHaveSize 3
      HostingStack["two"].value shouldBe delegatedHost
    }
    then("invalidate hosting") {
      HostingStack["two"].invalidate()
      shouldNotThrow<IllegalStateException> { delegatedHost }
    }
    then("hosting new") {
      HostingStack.take<List<Int>>("two").value = emptyList()
      delegatedHost shouldHaveSize 0
      delegatedHost.shouldBeEmpty()
    }
    then("remove the hosting from stack") {
      HostingStack.remove("two")
      HostingStack.find("two").shouldBeNull()
      shouldNotThrowAny { delegatedHost.toString() }
    }
  }
})