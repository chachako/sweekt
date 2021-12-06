import com.meowool.sweekt.datetime.nowDateTime
import com.meowool.sweekt.datetime.nowInstant
import com.meowool.sweekt.datetime.toDateTime
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.kotlinx.datetime.shouldBeAfter
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameDayAs
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameMonthAs
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameYearAs
import io.kotest.matchers.shouldBe

/**
 * Tests for /datetime
 *
 * @author 凛 (https://github.com/RinOrz)
 */
class DateTimeTests : StringSpec({
  val instant = nowInstant
  val dateTime = nowDateTime

  "effective instant time" {
    val instantTime = instant.toDateTime()
    nowInstant shouldBeAfter instant
    dateTime shouldHaveSameYearAs instantTime
    dateTime shouldHaveSameMonthAs instantTime
    dateTime shouldHaveSameDayAs instantTime
    dateTime.second shouldBe instantTime.second
  }
})