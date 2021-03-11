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
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.view.WindowCompat
import com.meowbase.toolkit.that
import com.meowbase.ui.UIBody
import com.meowbase.ui.Ui
import com.meowbase.ui.UiPreview
import com.meowbase.ui.animation.core.MotionDirection
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.skeleton.animation.SkeletonTransition
import com.meowbase.ui.widget.Button
import com.meowbase.ui.widget.ScrollableColumn
import com.meowbase.ui.widget.modifier.background
import com.meowbase.ui.widget.modifier.margin
import com.meowbase.ui.widget.modifier.safeArea

/**
 * [Skeleton] 转场动画案例
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/10 - 12:41
 */
class TransitionsSkeleton : Skeleton() {
  override val modifier: Modifier = Modifier.background(Color(0xff607D8B))
  override var uiBody: UIBody = {
    ScrollableColumn(
      crossAxisAlign = CrossAxisAlignment.Center,
      modifier = Modifier.safeArea(top = true)
    ) {
      Buttons(SkeletonTransition.Push)
      Buttons(SkeletonTransition.Slide)
      Buttons(SkeletonTransition.Cover)
      Buttons(SkeletonTransition.Page)
      Buttons(SkeletonTransition.ZoomSlide)
      Buttons(SkeletonTransition.Zoom, hasDirection = false)
      Buttons(SkeletonTransition.Fade, hasDirection = false)
      Buttons(SkeletonTransition.SharedElement, hasDirection = false)
      Buttons(SkeletonTransition.None, hasDirection = false)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
  }

  private fun Ui.Buttons(transition: SkeletonTransition, hasDirection: Boolean = true) {
    val name = transition::class.simpleName
    Button(name, transition, hasDirection)
    if (!hasDirection) return
    Button(name, transition + MotionDirection.EndToStart)
    Button(name, transition + MotionDirection.TopToDown)
    Button(name, transition + MotionDirection.BottomToUp)
  }

  private fun Ui.Button(
    transitionName: String?,
    transition: SkeletonTransition,
    hasDirection: Boolean = true
  ) = Button(
    text = !hasDirection that transitionName
      ?: transitionName + " & " + transition.direction.name,
    modifier = Modifier.margin(8.dp),
    textColor = Color.White,
    color = Color(0xff03A9F4),
  ) {
    defaultPushTransition = transition
    push(AnimationSkeleton(transitionName).apply {
      defaultPopTransition = transition.reverse()
    })
  }
}

@Deprecated(UiPreview.Deprecated)
private class TransitionsSkeletonPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, ::TransitionsSkeleton)