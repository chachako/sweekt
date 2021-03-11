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

package com.rin.ui.samples.transition

import android.content.Context
import android.util.AttributeSet
import com.meowbase.ui.UIBody
import com.meowbase.ui.UiPreview
import com.meowbase.ui.core.Alignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.currentTheme
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.widget.Text
import com.meowbase.ui.widget.modifier.background

/**
 * 通用的动画骨架
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/11 - 22:54
 */
class AnimationSkeleton(transitionName: String?) : Skeleton() {
  override val modifier: Modifier = Modifier.background(Color(0xfff48fb1))
  override var uiBody: UIBody = {
    Text(
      transitionName,
      color = Color(0xff1f1116),
      align = Alignment.Center,
      style = currentTheme.typography.h2,
    )
  }
}

@Deprecated(UiPreview.Deprecated)
private class AnimationSkeletonPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, { AnimationSkeleton(null) })