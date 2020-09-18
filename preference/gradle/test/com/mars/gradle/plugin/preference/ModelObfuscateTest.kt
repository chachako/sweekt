@file:Suppress("NestedLambdaShadowedImplicitParameter")

package com.mars.gradle.plugin.preference

import com.mars.ktcompiler.classesOrObjects
import com.mars.ktcompiler.defaultPsiFileFactory
import com.mars.ktcompiler.filterClassOrObject
import com.mars.ktcompiler.resolveKotlinFile
import sun.reflect.ReflectionFactory
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.extensions.ExtensionPoint
import org.jetbrains.kotlin.com.intellij.openapi.extensions.Extensions.getRootArea
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.util.UserDataHolderBase
import org.jetbrains.kotlin.com.intellij.pom.PomModel
import org.jetbrains.kotlin.com.intellij.pom.PomModelAspect
import org.jetbrains.kotlin.com.intellij.pom.PomTransaction
import org.jetbrains.kotlin.com.intellij.pom.impl.PomTransactionBase
import org.jetbrains.kotlin.com.intellij.pom.tree.TreeAspect
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.TreeCopyHandler
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.junit.Test

class ModelObfuscateTest {

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

  @Test
  fun parse() {
    defaultPsiFileFactory
      .resolveKotlinFile(codeCase)
      .filterClassOrObject(byAnnotation = {
        it.shortName?.identifier?.endsWith("VaguedPref") == true
      })
      .forEach {
        println(it.javaClass)
      }
  }

  companion object {
    private const val codeCase = """
      object NightMode : SharedPrefModel(mode = 2) {
        var itIs by boolean(false)
        var autoSwitch by boolean(true)

        var startHour by string("22")
        var startMinute by string("00")

        var endHour by string("07")
        val endMinute by string("30")
      }
      
      @VaguedPref
      class VagueNightMode() : SharedPrefModel(mode = 2) {
        var itIs by boolean(false)
        var autoSwitch by boolean(true)

        var startHour by string("22")
        var startMinute by string("00")

        var endHour by string("07")
        val endMinute by string("30")
      }
    """
  }
}
