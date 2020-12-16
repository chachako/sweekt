@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

package com.meowbase.gradle.plugin.preference.hooks

import com.meowbase.gradle.plugin.preference.PreferencePlugin.Companion.sourcesFileBackup
import com.meowbase.gradle.plugin.preference.data.VagueOptions
import com.meowbase.gradle.plugin.preference.data.tempDir
import com.meowbase.gradle.plugin.toolkit.hooker.TaskAction
import com.meowbase.gradle.plugin.toolkit.hooker.TaskHooker
import com.meowbase.gradle.plugin.toolkit.ktx.baseExtension
import com.meowbase.gradle.plugin.toolkit.ktx.forEachRecursive
import com.meowbase.ktcompiler.defaultProject
import com.meowbase.ktcompiler.psi.*
import com.meowbase.toolkit.existOrMkdir
import com.meowbase.toolkit.okio.*
import main
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import java.io.File

class VaguePreferences : TaskHooker("preBuild") {
  private val options: VagueOptions by lazy { project.extensions.getByType(VagueOptions::class.java) }

  override fun onBefore(): TaskAction? = {
    sourcesFileBackup.clear()
    project.baseExtension.sourceSets.main.java.srcDirs.forEachRecursive(::filter) { file ->
      var fileSource = file.readText()

      // 在解析源码前快速判断一遍
      if (!fileSource.contains("VaguePreference")) return@forEachRecursive

      // 备份源码到临时路径中
      val backup = project.tempDir.resolve(file.absolutePath).existOrMkdir(force = true)
      file.renameTo(backup)
      sourcesFileBackup.add(backup)
      logInfo("${backup.absolutePath} 找到 @VaguePreference, 开始混淆")

      defaultProject.ktPsiFactory
        .createFile(fileSource)
        .filterClassOrObject(byAnnotation = {
          it.shortName?.identifier?.endsWith("VaguePreference") == true
        })
        .forEach {
          var currentClassCode = it.text

          // 混淆名称
          currentClassCode = it.obfuscateName(currentClassCode)

          // 混淆属性
          currentClassCode = it.obfuscateProperties(currentClassCode)

          // 替换当前类源码到文件中
          fileSource = fileSource.replace(oldValue = it.text, newValue = currentClassCode)
        }

      logInfo("混淆完成 -> ${file.absolutePath}")

      // 写入新源码
      file.writeText(fileSource)
    }
  }


  /**
   * 对 Preference 模型名称进行混淆
   * @param sourceCode 将类封闭的源码
   * @return 返回混淆模型名称后的源码
   */
  private fun KtClassOrObject.obfuscateName(sourceCode: String): String {
    var newSourceCode = sourceCode
    val superTypeCall = superTypeCall ?: return newSourceCode
    val superCallText = superTypeCall.text
    val superCallArgs = superTypeCall.valueArguments

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
      newSourceCode = newSourceCode.replaceFirst(
        oldValue = superCallText,
        newValue = superCallText.addCallArgs("name = \"${name!!.obfuscated()}\""),
      )
    }

    return newSourceCode
  }


  /**
   * 对所有委托属性进行混淆
   * @param sourceCode 将委托属性封闭的源码
   * @return 返回混淆属性后的源码
   */
  private fun KtClassOrObject.obfuscateProperties(sourceCode: String): String {
    var newSourceCode = sourceCode
    // 找出所有需要混淆的属性
    body?.filterProperties(byDelegateExpression = {
      when {
        /**
         * 如果 delegation 的 initializer 中定义了 key 参数，或者第二个是字符串时，不加密当前属性在 preference 文件中对应的名称
         * ```
         * // 将会加密 'isNight' 后才会写入 preference 文件中
         * var isNight by boolean(false)
         *
         * // 将会以 'darkMode' 写入到 preference 文件中
         * var isNight by boolean(false, key = "darkMode")
         * ```
         */
        it is KtCallExpression && options.delegation.contains(it.firstChild.text) -> {
          it.valueArguments.getOrNull(1)?.getArgumentExpression() !is KtStringTemplateExpression
            && !it.valueArguments.hasArgumentName("key")
        }
        else -> false
      }
    })?.forEach {
      newSourceCode = newSourceCode.replace(
        oldValue = it.text,
        newValue = it.text.addCallArgs("key = \"${it.name!!.obfuscated()}\"")
      )
    }
    return newSourceCode
  }


  /** 利用 [VagueOptions.dictionary] 混淆字符串 */
  private fun String.obfuscated(): String {
    var fogged = ""
    forEach { fogged += options.dictionary[it.toLowerCase()] ?: this }
    return fogged
  }

  private fun String.addCallArgs(join: String) = "${trimEnd(')')}, $join)"

  /** 只检索 kt 文件，因为 Kotpref 只支持 Kotlin 语言 */
  private fun filter(file: File): Boolean = file.extension == ".kt"



// FIXME 也许确实应该使用 AST 访问，而不是原生的 Kotlin compiler api

//  class VaguePreferences : AbstractProxy() {
//    override fun doFirst(): TaskAction? = {
//      sourcesFileBackup.clear()
//      taskExtension.sourceSets["main"].java.srcDirs.forEachRecursive { file ->
//        val oldSource = file.readText()
//        // 用 visitor 访问前快速判断一遍
//        if (!oldSource.contains("VaguedPref")) return@forEachRecursive
//        val backup = File(tempDir, file.absolutePath)
//        println("      Operation ${backup.absolutePath}")
//        // FIXME 有几率出现错误 java.lang.NoClassDefFoundError: com/sun/jna/Native，实际上不会有影响
//        try {
//          var className = ""
//          var isKotprefModel = false
//          val ast = MutableVisitor.preVisit(Parser.parseFile(oldSource)) { line, parent ->
//            // 确保当前类被注解
//            MutableVisitor.preVisit(parent) { v, _ ->
//              if (v is Node.Modifier.AnnotationSet.Annotation) {
//                isKotprefModel = v.names.contains("VaguedPref")
//              }; v
//            }
//            when {
//              isKotprefModel -> {
//                if (line is Node.Decl.Structured) className = line.name
//                rewrite(
//                  className.replaceFirst("_", "").replaceFirst("Pref", ""),
//                  line
//                )
//              }
//              else -> line
//            }
//          }
//          val newSource = Writer.write(ast)
//          // 将源码备份
//          backup.also {
//            it.parentFile.mkdirs()
//            file.renameTo(it)
//            sourcesFileBackup.add(it)
//          }
//          // 写入新源码
//          file.writeText(newSource)
//        } catch (_: ClassNotFoundException) {
//          /** ignored */
//        } catch (_: NoClassDefFoundError) {
//          /** ignored */
//        }
//      }
//    }
//
//    @Suppress("NAME_SHADOWING")
//    fun rewrite(className: String, line: Node?): Node? {
//      var line = line
//      if (line is Node.Decl.Structured.Parent.CallConstructor) {
//        val pass = line.args.firstOrNull { it.name == "name" } != null &&
//          line.args.getOrNull(0)?.expr is Node.Expr.StringTmpl
//        if (!pass) {
//          var preferenceName = ""
//          className.forEach { preferenceName += it.encode() }
//          // 如果 super 构造函数没有传入 prefName 则根据类名写入
//          line = line.copy(
//            args = listOf(
//              Node.ValueArg(
//                "name", false, Node.Expr.StringTmpl(
//                  listOf(Node.Expr.StringTmpl.Elem.Regular(preferenceName)), false
//                )
//              )
//            ) + line.args
//          )
//        }
//      }
//      if (line is Node.Decl.Property && line.delegated) {
//        var newExpr = line.expr
//        if (newExpr is Node.Expr.Call) {
//          // 确保参数第二个或者参数列表中存在 key 传入
//          val pass = newExpr.args.firstOrNull { it.name == "key" } != null &&
//            newExpr.args.getOrNull(1)?.expr is Node.Expr.StringTmpl
//          if (!pass) {
//            var preferenceName = ""
//            line.vars[0]!!.name.forEach { preferenceName += it.encode() }
//            // 如果没有传入 key 则生成一个混淆后的属性名称并传入
//            newExpr = newExpr.copy(
//              args = newExpr.args.toMutableList().apply {
//                add(
//                  1, Node.ValueArg(
//                    "key", false, Node.Expr.StringTmpl(
//                      listOf(Node.Expr.StringTmpl.Elem.Regular(preferenceName)),
//                      false
//                    )
//                  )
//                )
//              }
//            )
//          }
//        }
//        line = line.copy(expr = newExpr)
//      }
//      return line
//    }
//
//    private fun Char.encode(): Char {
//      return when (toLowerCase()) {
//        'a' -> 'ִ'
//        'b' -> 'ׁ'
//        'c' -> 'ׅ'
//        'd' -> 'ܼ'
//        'e' -> '࡛'
//        'f' -> 'ٖ'
//        'g' -> '݈'
//        'h' -> '˙'
//        'i' -> '໋'
//        'j' -> '֒'
//        'k' -> '݁'
//        'l' -> 'ؚ'
//        'm' -> '՝'
//        'n' -> '՛'
//        'o' -> '՟'
//        'p' -> 'ܿ'
//        'q' -> 'ּ'
//        'r' -> 'វ'
//        's' -> '٬'
//        't' -> '݇'
//        'u' -> '༹'
//        'v' -> '་'
//        'w' -> 'ܳ'
//        'x' -> '݅'
//        'y' -> 'ࠫ'
//        'z' -> 'ࠣ'
//        else -> this
//      }
//    }
//
//    /** 只检索 kt 文件，因为 Kotpref 只支持 Kotlin 语言 */
//    override fun File.filter(): Boolean = name.endsWith(".kt")
//  }
}