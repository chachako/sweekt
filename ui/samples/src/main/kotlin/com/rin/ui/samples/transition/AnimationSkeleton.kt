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