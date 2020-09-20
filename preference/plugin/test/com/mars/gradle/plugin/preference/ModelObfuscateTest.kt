@file:Suppress("NestedLambdaShadowedImplicitParameter")

package com.mars.gradle.plugin.preference

import com.mars.ktcompiler.defaultProject
import com.mars.ktcompiler.psi.*
import org.jetbrains.kotlin.com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.psi.*
import org.junit.Test

class ModelObfuscateTest {

  private fun String.fog(): String {
    fun Char.encode(): Char {
      return when (toLowerCase()) {
        'a' -> 'ִ'
        'b' -> 'ׁ'
        'c' -> 'ׅ'
        'd' -> 'ܼ'
        'e' -> '࡛'
        'f' -> 'ٖ'
        'g' -> '݈'
        'h' -> '˙'
        'i' -> '໋'
        'j' -> '֒'
        'k' -> '݁'
        'l' -> 'ؚ'
        'm' -> '՝'
        'n' -> '՛'
        'o' -> '՟'
        'p' -> 'ܿ'
        'q' -> 'ּ'
        'r' -> 'វ'
        's' -> '٬'
        't' -> '݇'
        'u' -> '༹'
        'v' -> '་'
        'w' -> 'ܳ'
        'x' -> '݅'
        'y' -> 'ࠫ'
        'z' -> 'ࠣ'
        else -> this
      }
    }
    var fogged = ""
    forEach { fogged += it.encode() }
    return fogged
  }

  private fun String.addCallArgs(join: String) = "${trimEnd(')')}, $join)"

  @Test
  fun parse() {
    var codeCase = codeCase

    defaultProject.ktPsiFactory
      .createFile(codeCase)
      .filterClassOrObject(byAnnotation = {
        it.shortName?.identifier?.endsWith("VaguePreference") == true
      })
      .forEach {
        var newClassCode = it.text
        val superCallArgs: List<ValueArgument> = it.superTypeCall?.valueArguments ?: return@forEach
        /**
         * 如果 SuperPrefModel 的构造调用中定义了 name 参数，或者第二个是字符串时，不加密 preference 文件的名称
         * ```
         * // 'PrefExample1' 的储存文件名称将会以 'PrefExample1' 加密
         * @FoggingPref object PrefExample1 : SharedPrefModel()
         *
         * // 'PrefExample2' 的储存文件将会命名为 PrefName
         * @FoggingPref object PrefExample2 : SharedPrefModel(mode = 2, name = "PrefName")
         * ```
         */
        val isFogPrefName = !superCallArgs.hasArgumentName("name") ||
          superCallArgs.getOrNull(1)?.getArgumentExpression() !is KtStringTemplateExpression

        if (isFogPrefName) {
          // 进行混淆
          val newProperty = it.superTypeCall!!.text.addCallArgs("name = \"${it.name!!.fog()}\"")
          newClassCode = newClassCode.replaceFirst(it.superTypeCall!!.text, newProperty)
        }

        // 找出所有需要混淆的属性
        it.body?.filterProperties(byDelegateExpression = {
          when {
            // 确保 delegation 的 initializer 没有定义 key 参数，且调用的参数的第二个不是字符串
            it is KtCallExpression && it.firstChild.text == "boolean" -> {
              it.valueArguments.getOrNull(1)?.getArgumentExpression() !is KtStringTemplateExpression
                && !it.valueArguments.hasArgumentName("key")
            }
            else -> false
          }
        })?.forEach {
          val newProperty = it.text.addCallArgs("key = \"${it.name!!.fog()}\"")
          newClassCode = newClassCode.replace(it.text, newProperty)
        }

        codeCase = codeCase.replace(it.text, newClassCode)
      }

    println(codeCase)
  }

  companion object {
    private const val codeCase = """
      object NightMode : SharedPrefModel(mode = 2) {
        var using by boolean(false)
        var autoSwitch by boolean(true)

        var startHour by string("22")
        var startMinute by string("00")

        var endHour by string("07")
        val endMinute by string("30")
      }
      
      @VaguePreference
      class VagueNightMode(
      
      )     : SharedPrefModel(mode = 2 ) {
        var using by boolean(false, "using")
        var autoSwitch by boolean(true ) 

        var startHour by string("22")
        var startMinute by string("00")

        var endHour by string("07")
        val endMinute by string("30")
      }
    """
  }
}
