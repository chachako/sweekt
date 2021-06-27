import com.meowool.sweekt.datetime.currentHour
import com.meowool.sweekt.datetime.currentMinute
import com.meowool.sweekt.datetime.currentMonth
import com.meowool.sweekt.datetime.currentSecond
import com.meowool.sweekt.datetime.currentYear
import com.meowool.sweekt.datetime.format
import com.meowool.sweekt.datetime.isTimeInRange
import com.meowool.sweekt.datetime.nowDateTime
import com.meowool.sweekt.datetime.nowInstant
import com.meowool.sweekt.datetime.resolveToInstant
import com.meowool.sweekt.datetime.resolveToTime
import com.meowool.sweekt.datetime.toDateTime
import com.meowool.sweekt.datetime.todayOfMonth
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.date.shouldHaveSameDayAs
import io.kotest.matchers.date.shouldHaveSameMonthAs
import io.kotest.matchers.date.shouldHaveSameYearAs
import io.kotest.matchers.date.shouldNotBeAfter
import io.kotest.matchers.date.shouldNotBeBefore
import io.kotest.matchers.date.shouldNotHaveSameYearAs
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Month

/**
 * Tests for DataTimes.kt
 *
 * @author 凛 (https://github.com/RinOrz)
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
    val resolvedTime = "2020-2-11 07:00".resolveToTime("yyyy-M-d HH:mm")
    val resolvedInstant = "2020-2-11 07:00".resolveToInstant("yyyy-M-d HH:mm")
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
    val start = "2010-1-2".resolveToTime("yyyy-M-d")
    val stop = "2011-2-4".resolveToTime("yyyy-M-d")
    val early = "2010-11-12".resolveToTime("yyyy-M-d")
    val lately = "2111-5-8".resolveToTime("yyyy-M-d")

    start shouldNotBeAfter stop
    stop shouldNotBeBefore start

    early shouldBeAfter start
    early shouldBeBefore stop

    lately shouldBeAfter start
    lately shouldNotBeBefore stop

    isTimeInRange(time = early, start, stop) shouldBe true
    isTimeInRange(time = lately, start, stop) shouldBe false
  }

  "time in range" {
    val start = "2022-1-2 02:00".resolveToTime("yyyy-M-d HH:mm")
    val stop = "2022-1-2 22:54".resolveToTime("yyyy-M-d HH:mm")
    val inRange = "2022-1-2 07:00".resolveToTime("yyyy-M-d HH:mm")
    val notRange = "2022-1-2 00:00".resolveToTime("yyyy-M-d HH:mm")
    val unknownDate = "07:00".resolveToTime("HH:mm")

    start shouldHaveSameYearAs stop
    start shouldHaveSameMonthAs stop
    start shouldHaveSameDayAs stop

    inRange shouldNotBeAfter stop
    notRange shouldBeBefore  start

    unknownDate shouldNotHaveSameYearAs start
    unknownDate shouldNotHaveSameYearAs stop

    isTimeInRange(time = inRange, start, stop) shouldBe true
    isTimeInRange(time = notRange, start, stop) shouldBe false
    isTimeInRange(time = unknownDate, start, stop) shouldBe false
  }
})