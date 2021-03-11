/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("SpellCheckingInspection")

package com.meowbase.plugin.uikit

import com.meowbase.plugin.toolkit.booster.transform.TransformContext
import com.meowbase.plugin.toolkit.booster.transform.asm.ClassTransformer
import com.meowbase.plugin.uikit.Constants.uikitViews
import com.meowbase.plugin.uikit.Constants.systemViews
import com.meowbase.plugin.uikit.Constants.whitelist
import org.objectweb.asm.tree.*

/*
 * author: 凛
 * date: 2020/8/12 4:55 PM
 * github: https://github.com/RinOrz
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