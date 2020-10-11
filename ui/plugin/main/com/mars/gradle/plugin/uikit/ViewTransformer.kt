@file:Suppress("SpellCheckingInspection")

package com.mars.gradle.plugin.uikit

import com.mars.gradle.plugin.toolkit.booster.transform.TransformContext
import com.mars.gradle.plugin.toolkit.booster.transform.asm.ClassTransformer
import com.mars.gradle.plugin.uikit.Constants.uikitViews
import com.mars.gradle.plugin.uikit.Constants.systemViews
import com.mars.gradle.plugin.uikit.Constants.whitelist
import org.objectweb.asm.tree.*

/*
 * author: 凛
 * date: 2020/8/12 4:55 PM
 * github: https://github.com/oh-Rin
 * description: View 转换器，用于将所有 View(不包括 UiKit) 转换为 UiKit 系列 Views
 */
class ViewTransformer : ClassTransformer {
  override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
    // we just need to transform the classes of extends android view
    if (systemViews.contains(klass.superName)
      // don't transform uikit views classes
      && !uikitViews.contains(klass.name)
      // don't transform uikit parent view classes
      && !whitelist.contains(klass.name)
    ) {
      val originSuperName = klass.superName
      // replace superclass
      klass.superName = klass.superName.remapping()
      // replace all call-instructions
      klass.methods.forEach {
        it.instructions.toArray().forEach { insn ->
          // replace only owner with same name as original superclass
          if (insn is MethodInsnNode && insn.owner == originSuperName) {
            insn.owner = insn.owner.remapping()
          }
        }
      }
//      // replaced, print current calss name
//      println("Transforming ${klass.name}")
    }
    return klass
  }

  private fun String.remapping() = when (this) {
    Views.ViewGroup.system -> Views.ViewGroup.uikit
    Views.ImageView.system -> Views.ImageView.uikit
    Views.TextView.system -> Views.TextView.uikit
    Views.LinearLayout.system -> Views.LinearLayout.uikit
    Views.FrameLayout.system -> Views.FrameLayout.uikit
    else -> this
  }
}