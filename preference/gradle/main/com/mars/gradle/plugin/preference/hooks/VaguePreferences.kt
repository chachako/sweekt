@file:Suppress("SpellCheckingInspection")

package com.mars.gradle.plugin.preference.hooks

import com.mars.gradle.plugin.toolkit.hooker.TaskAction
import com.mars.gradle.plugin.toolkit.hooker.TaskHooker

class VaguePreferences : TaskHooker("preBuild") {
  override fun onBefore(): TaskAction? {
    return super.onBefore()
  }

  companion object {
    const val taskName = "preBuild"
  }

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