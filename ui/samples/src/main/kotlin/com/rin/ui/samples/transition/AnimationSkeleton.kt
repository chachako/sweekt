package com.rin.ui.samples.transition

import android.content.Context
import android.util.AttributeSet
import com.mars.ui.UIBody
import com.mars.ui.UiPreview
import com.mars.ui.core.Alignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.currentTheme
import com.mars.ui.skeleton.Skeleton
import com.mars.ui.widget.Text
import com.mars.ui.widget.modifier.background

/**
 * 通用的动画骨架
 *
 * @author 凛
 * @github https://github.com/oh-Rin
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