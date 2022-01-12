import com.meowool.sweekt.datetime.currentHour
import com.meowool.sweekt.datetime.currentMinute
import com.meowool.sweekt.datetime.currentMonth
import com.meowool.sweekt.datetime.currentSecond
import com.meowool.sweekt.datetime.currentYear
import com.meowool.sweekt.datetime.format
import com.meowool.sweekt.datetime.inRange
import com.meowool.sweekt.datetime.nowDateTime
import com.meowool.sweekt.datetime.nowInstant
import com.meowool.sweekt.datetime.toDateTime
import com.meowool.sweekt.datetime.toInstant
import com.meowool.sweekt.datetime.todayOfMonth
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.kotlinx.datetime.shouldBeAfter
import io.kotest.matchers.kotlinx.datetime.shouldBeBefore
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameDayAs
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameMonthAs
import io.kotest.matchers.kotlinx.datetime.shouldHaveSameYearAs
import io.kotest.matchers.kotlinx.datetime.shouldNotBeAfter
import io.kotest.matchers.kotlinx.datetime.shouldNotBeBefore
import io.kotest.matchers.kotlinx.datetime.shouldNotHaveSameYearAs
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Month

/**
 * Tests for DataTimes.kt
 *
 * @author 凛 (RinOrz)
 */
class DataTimeTests : StringSpec({
  val instant = nowInstant
  val dateTime = nowDateTime

  "effective instant time" {
    val instantTime = instant.toDateTime()

    dateTime shouldHaveSameYearAs instantTime
    dateTime shouldHaveSameMonthAs instantTime
    dateTime shouldHaveSameDayAs instantTime
    dateTime.second shouldBe instantTime.second
  }

  "resolved date time string" {
    val resolvedTime = "2020-2-11 07:00".toDateTime("yyyy-M-d HH:mm")
    val resolvedInstant = "2020-2-11 07:00".toInstant("yyyy-M-d HH:mm")
    val resolvedInstantTime = resolvedInstant.toDateTime()

    resolvedTime.toInstant() shouldBe resolvedInstant
    resolvedTime shouldHaveSameYearAs resolvedInstantTime
    resolvedTime shouldHaveSameMonthAs resolvedInstantTime
    resolvedTime shouldHaveSameDayAs resolvedInstantTime
    resolvedTime should {
      it.year shouldBe 2020
      it.month shouldBe Month.FEBRUARY
      it.dayOfMonth shouldBe 11
      it.hour shouldBe 7
      it.minute shouldBe 0
      it.second shouldBe 0
    }
  }

  "correct formatting date time" {
    val dateFormatted = dateTime.format("yyyy-M-d H:m:s")

    dateFormatted shouldBe instant.format("yyyy-M-d H:m:s")
    dateFormatted shouldBe "$currentYear-$currentMonth-$todayOfMonth $currentHour:$currentMinute:$currentSecond"
  }

  "date in range" {
    val start = "2010-1-2".toDateTime("yyyy-M-d")
    val stop = "2011-2-4".toDateTime("yyyy-M-d")
    val early = "2010-11-12".toDateTime("yyyy-M-d")
    val lately = "2111-5-8".toDateTime("yyyy-M-d")

    start shouldNotBeAfter stop
    stop shouldNotBeBefore start

    early shouldBeAfter start
    early shouldBeBefore stop

    lately shouldBeAfter start
    lately shouldNotBeBefore stop

    early.inRange(start, stop) shouldBe true
    lately.inRange(start, stop) shouldBe false
  }

  "time in range" {
    val start = "2022-1-2 02:00".toDateTime("yyyy-M-d HH:mm")
    val stop = "2022-1-2 22:54".toDateTime("yyyy-M-d HH:mm")
    val inRange = "2022-1-2 07:00".toDateTime("yyyy-M-d HH:mm")
    val notRange = "2022-1-2 00:00".toDateTime("yyyy-M-d HH:mm")
    val unknownDate = "07:00".toDateTime("HH:mm")

    start shouldHaveSameYearAs stop
    start shouldHaveSameMonthAs stop
    start shouldHaveSameDayAs stop

    inRange shouldNotBeAfter stop
    notRange shouldBeBefore  start

    unknownDate shouldNotHaveSameYearAs start
    unknownDate shouldNotHaveSameYearAs stop

    inRange.inRange(start, stop) shouldBe true
    notRange.inRange(start, stop) shouldBe false
    unknownDate.inRange(start, stop) shouldBe false
  }
})