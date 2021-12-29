import com.meowool.sweekt.toReadableSize
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * @author 凛 (RinOrz)
 */
class SizeTests : StringSpec({
  "readable size with Binary" {
    1000.toReadableSize(useSI = false).shouldBe("1000 B")
    1023.toReadableSize(useSI = false).shouldBe("1023 B")
    1024.toReadableSize(useSI = false).shouldBe("1.00 KiB")
    1728.toReadableSize(useSI = false).shouldBe("1.69 KiB")
    110592.toReadableSize(useSI = false).shouldBe("108.00 KiB")
  }
  "readable size with SI" {
    1000.toReadableSize(useSI = true).shouldBe("1.00 KB")
    1023.toReadableSize(useSI = true).shouldBe("1.02 KB")
    1024.toReadableSize(useSI = true).shouldBe("1.02 KB")
    1728.toReadableSize(useSI = true).shouldBe("1.73 KB")
    110592.toReadableSize(useSI = true).shouldBe("110.59 KB")
  }
  "readable size with specified precision" {
    1000.toReadableSize(precision = 0, useSI = false).shouldBe("1000 B")
    1000.toReadableSize(precision = 1, useSI = false).shouldBe("1000 B")

    1728.toReadableSize(precision = 0, useSI = false).shouldBe("2 KiB")
    1728.toReadableSize(precision = 1, useSI = false).shouldBe("1.7 KiB")
    1728.toReadableSize(precision = 1, useSI = true).shouldBe("1.7 KB")

    110592.toReadableSize(precision = 0, useSI = false).shouldBe("108 KiB")
    110592.toReadableSize(precision = 1, useSI = false).shouldBe("108.0 KiB")
    110592.toReadableSize(precision = 1, useSI = true).shouldBe("110.6 KB")
  }
})
