import com.meowool.sweekt.isChinese
import com.meowool.sweekt.isChineseNotPunctuation
import com.meowool.sweekt.isChinesePunctuation
import com.meowool.sweekt.isEnglish
import com.meowool.sweekt.isEnglishNotPunctuation
import com.meowool.sweekt.isEnglishPunctuation
import com.meowool.sweekt.isPunctuation
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class CharTests : StringSpec({
  "isChinese" {
    '汉'.isChinese() shouldBe true
    '働'.isChinese() shouldBe true
    'み'.isChinese() shouldBe false
    '목'.isChinese() shouldBe false
    '，'.isChinese() shouldBe true
    '。'.isChinese() shouldBe true
    'a'.isChinese() shouldBe false
  }
  "isChinesePunctuation" {
    '？'.isChinesePunctuation() shouldBe true
    '?'.isChinesePunctuation() shouldBe false
  }
  "isChineseNotPunctuation" {
    '凛'.isChineseNotPunctuation() shouldBe true
    '，'.isChineseNotPunctuation() shouldBe false
    ','.isChineseNotPunctuation() shouldBe false
  }

  "isEnglish" {
    'a'.isEnglish() shouldBe true
    '.'.isEnglish() shouldBe true
    '汉'.isEnglish() shouldBe false
  }
  "isEnglishPunctuation" {
    '@'.isEnglishPunctuation() shouldBe true
    '-'.isEnglishPunctuation() shouldBe true
    'O'.isEnglishPunctuation() shouldBe false
    '。'.isEnglishPunctuation() shouldBe false
  }
  "isEnglishNotPunctuation" {
    'O'.isEnglishNotPunctuation() shouldBe true
    '.'.isEnglishNotPunctuation() shouldBe false
  }

  "isPunctuation" {
    ','.isPunctuation() shouldBe true
    ','.isPunctuation() shouldBe true
    '？'.isPunctuation() shouldBe true
    '-'.isPunctuation() shouldBe true
    '、'.isPunctuation() shouldBe true
    '\\'.isPunctuation() shouldBe true
    '@'.isPunctuation() shouldBe true
    '^'.isPunctuation() shouldBe true
  }
})