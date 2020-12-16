package com.meowbase.ui.skeleton.owners

import com.meowbase.toolkit.millis
import com.meowbase.toolkit.view.scale
import com.meowbase.ui.animation.Easing
import com.meowbase.ui.animation.Motion
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.core.MotionDirection
import com.meowbase.ui.animation.definition.*
import com.meowbase.ui.animation.startMotion
import com.meowbase.ui.core.unit.px
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.skeleton.animation.SkeletonTransition

/**
 * 代表导航的过渡变化后的结束回调，用于在更改完成时发出通知
 *
 * @see TransitionOwner.onPushChange
 * @see TransitionOwner.onPopChange
 */
typealias TransitionChange = () -> Unit


/**
 * 负责处理 [Skeleton] 之间的过渡动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/4 - 22:35
 */
interface TransitionOwner: SkeletonOwner {
  var defaultPushTransition: SkeletonTransition?
  var defaultPopTransition: SkeletonTransition?

  /**
   * 处理调用 [NavigationOwner.push] 后的过渡动画
   *
   * @param next 跳转的目标
   * @param complete 当动画完成后必须要调用 `complete()` 以通知 Skeleton 完成变化
   */
  fun onPushChange(next: Skeleton, complete: TransitionChange) {
    val transition = defaultPushTransition!!
    val currentWidth = current.view.width
    val currentHeight = current.view.height

    startMotion(
      300.millis,
      easing = Easing.FastOutSlowIn,
      onCompletion = {
        complete()
        // 在跳转完成后，上一个界面必须保证恢复原状，否则下一个界面返回上一个界面时动画会出现问题
        with(current.view) {
          scale = 1f
          translationX = 1f
          translationY = 1f
          alpha = 1f
        }
      }
    ) {
      fun slide(offsetX: Int = currentWidth, offsetY: Int = currentHeight, appear: Boolean) =
        slide(transition.direction, offsetX, offsetY, appear)

      when (transition) {
        SkeletonTransition.Push -> {
          current.view += slide(currentWidth / 3, appear = false) + fadeOut(0.2f)
          next.view += slide(appear = true)
        }
        SkeletonTransition.Slide -> {
          current.view += slide(appear = false)
          next.view += slide(appear = true)
        }
        SkeletonTransition.ZoomSlide -> {
          current.view += slide(currentWidth / 2, appear = false) + zoomOut(0.8f) + fadeOut(0.2f)
          next.view += slide(appear = true) + zoomIn(0.8f)
        }
        SkeletonTransition.Cover -> {
          next.view += slide(appear = true)
          current.view += fadeOut(0.2f)
        }
        SkeletonTransition.Page -> {
          next.view += slide(appear = true)
          current.view += zoomOut(0.7f) + fadeOut(0.2f)
        }
        SkeletonTransition.Fade -> {
          next.view += fadeIn(0f)
        }
        SkeletonTransition.Zoom -> {
          next.view += zoomIn(0.8f) + fadeIn(0f)
          current.view += zoomOut(1.2f) + fadeOut(0.2f)
        }
        else -> {
        }
      }
    }
  }

  /**
   * 处理调用 [NavigationOwner.pop] 后的过渡动画
   *
   * @param prev 上一个界面 - 当前界面销毁后显示的栈顶界面（如果有）
   * @param complete 当动画完成后必须要调用 `complete()` 以通知 Skeleton 完成变化
   */
  fun onPopChange(prev: Skeleton?, complete: TransitionChange) {
    val transition = defaultPopTransition!!
    val currentWidth = current.view.width
    val currentHeight = current.view.height

    startMotion(
      250.millis,
      easing = Easing.EaseOutSine,
      onCompletion = { complete() }
    ) {
      fun slide(offsetX: Int = currentWidth, offsetY: Int = currentHeight, appear: Boolean) =
        slide(transition.direction, offsetX, offsetY, appear)

      when (transition) {
        SkeletonTransition.Push -> {
          prev?.view += slide(currentWidth / 3, appear = true) + fadeIn(0.2f)
          current.view += slide(appear = false)
        }
        SkeletonTransition.Slide -> {
          prev?.view += slide(appear = true)
          current.view += slide(appear = false)
        }
        SkeletonTransition.ZoomSlide -> {
          prev?.view += slide(currentWidth / 2, appear = true) + zoomIn(0.8f) + fadeIn(0.2f)
          current.view += slide(appear = false) + zoomOut(0.8f)
        }
        SkeletonTransition.Cover -> {
          prev?.view += fadeIn(0.2f)
          current.view += slide(appear = false)
        }
        SkeletonTransition.Page -> {
          prev?.view += zoomIn(0.7f) + fadeIn(0.2f)
          current.view += slide(appear = false)
        }
        SkeletonTransition.Fade -> {
          current.view += fadeOut(0f)
        }
        SkeletonTransition.Zoom -> {
          prev?.view += zoomIn(1.2f) + fadeIn(0f)
          current.view += zoomOut(0.8f) + fadeOut(0f)
        }
        else -> {
        }
      }
    }
  }
}


private fun Motion.slide(
  direction: MotionDirection,
  offsetX: Int,
  offsetY: Int,
  appear: Boolean
): AnimationDefinition = when (direction) {
  MotionDirection.StartToEnd -> if (appear) {
    slideInX(initialOffset = -offsetX.px)
  } else {
    slideOutX(targetOffset = offsetX.px)
  }
  MotionDirection.EndToStart -> if (appear) {
    slideInX(initialOffset = offsetX.px)
  } else {
    slideOutX(targetOffset = -offsetX.px)
  }
  MotionDirection.TopToDown -> if (appear) {
    slideInY(initialOffset = -offsetY.px)
  } else {
    slideOutY(targetOffset = offsetY.px)
  }
  MotionDirection.BottomToUp -> if (appear) {
    slideInY(initialOffset = offsetY.px)
  } else {
    slideOutY(targetOffset = -offsetY.px)
  }
}