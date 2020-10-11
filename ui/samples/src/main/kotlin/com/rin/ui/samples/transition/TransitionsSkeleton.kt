package com.rin.ui.samples.transition

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.view.WindowCompat
import com.mars.toolkit.that
import com.mars.ui.UIBody
import com.mars.ui.Ui
import com.mars.ui.UiPreview
import com.mars.ui.animation.core.MotionDirection
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.unit.dp
import com.mars.ui.skeleton.Skeleton
import com.mars.ui.skeleton.animation.SkeletonTransition
import com.mars.ui.widget.Button
import com.mars.ui.widget.ScrollableColumn
import com.mars.ui.widget.modifier.background
import com.mars.ui.widget.modifier.margin
import com.mars.ui.widget.modifier.safeArea

/**
 * [Skeleton] 转场动画案例
 *
 * @author 凛
 * @github https://github.com/oh-Rin
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