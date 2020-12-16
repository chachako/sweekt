@file:Suppress("FunctionName", "UNCHECKED_CAST")

package com.meowbase.toolkit.widget

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Px
import androidx.core.view.setMargins
import com.meowbase.toolkit.data.wrapContent
import com.meowbase.toolkit.NoGetter
import com.meowbase.toolkit.int
import com.meowbase.toolkit.noGetter

inline var ViewGroup.MarginLayoutParams.horizontalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    leftMargin = value
    rightMargin = value
  }

inline var ViewGroup.MarginLayoutParams.verticalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    topMargin = value
    bottomMargin = value
  }

inline var ViewGroup.MarginLayoutParams.margin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setMargins(value)

inline var ViewGroup.MarginLayoutParams.marginTop: Int
  get() = topMargin
  set(@Px value) {
    topMargin = value
  }

inline var ViewGroup.MarginLayoutParams.marginBottom: Int
  get() = bottomMargin
  set(@Px value) {
    bottomMargin = value
  }

val View.marginLayoutParamsOrNull: ViewGroup.MarginLayoutParams?
  get() = layoutParams as? ViewGroup.MarginLayoutParams

val View.relativeLayoutParamsOrNull: RelativeLayout.LayoutParams?
  get() = layoutParams as? RelativeLayout.LayoutParams

val View.frameLayoutParamsOrNull: FrameLayout.LayoutParams?
  get() = layoutParams as? FrameLayout.LayoutParams

val View.linearLayoutParamsOrNull: LinearLayout.LayoutParams?
  get() = layoutParams as? LinearLayout.LayoutParams

/** 返回 View 的或创建新的 [ViewGroup.LayoutParams] */
val View.LayoutParams: ViewGroup.LayoutParams
  get() = LayoutParams()

/** 返回 View 的或创建新的 [ViewGroup.MarginLayoutParams] */
val View.MarginLayoutParams: ViewGroup.MarginLayoutParams
  get() = MarginLayoutParams()

/** 返回 View 的或创建新的 [RelativeLayout.LayoutParams] */
val View.RelativeLayoutParams: RelativeLayout.LayoutParams
  get() = RelativeLayoutParams()

/** 返回 View 的或创建新的 [FrameLayout.LayoutParams] */
val View.FrameLayoutParams: FrameLayout.LayoutParams
  get() = FrameLayoutParams()

/** 返回 View 的或创建新的 [LinearLayout.LayoutParams] */
val View.LinearLayoutParams: LinearLayout.LayoutParams
  get() = LinearLayoutParams()

/**
 * 返回 View 的或创建新的 [ViewGroup.LayoutParams]
 * @param width View 的宽度
 * @param height View 的高度
 */
inline fun View.LayoutParams(
  width: Number = wrapContent,
  height: Number = wrapContent,
  block: ViewGroup.LayoutParams.() -> Unit = {}
): ViewGroup.LayoutParams = (layoutParams
  ?: ViewGroup.LayoutParams(width.int, height.int)).apply(block)

/**
 * 返回 View 的或创建新的 [ViewGroup.MarginLayoutParams]
 * @param width View 的宽度
 * @param height View 的高度
 */
inline fun View.MarginLayoutParams(
  width: Number = wrapContent,
  height: Number = wrapContent,
  block: ViewGroup.MarginLayoutParams.() -> Unit = {}
): ViewGroup.MarginLayoutParams = layoutParams.let {
  val lp = marginLayoutParamsOrNull
  when {
    lp == null && it != null -> ViewGroup.MarginLayoutParams(it)
    else -> lp ?: ViewGroup.MarginLayoutParams(width.int, height.int)
  }.apply(block)
}

/**
 * 返回 View 的或创建新的 [RelativeLayout.LayoutParams]
 * @param width View 的宽度
 * @param height View 的高度
 */
inline fun View.RelativeLayoutParams(
  width: Number = wrapContent,
  height: Number = wrapContent,
  block: RelativeLayout.LayoutParams.() -> Unit = {}
): RelativeLayout.LayoutParams = layoutParams.let {
  val lp = relativeLayoutParamsOrNull
  when {
    lp == null && it != null -> RelativeLayout.LayoutParams(it)
    else -> lp ?: RelativeLayout.LayoutParams(width.int, height.int)
  }.apply(block)
}

/**
 * 返回 View 的或创建新的 [FrameLayout.LayoutParams]
 * @param width View 的宽度
 * @param height View 的高度
 */
inline fun View.FrameLayoutParams(
  width: Number = wrapContent,
  height: Number = wrapContent,
  block: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams = layoutParams.let {
  val lp = frameLayoutParamsOrNull
  when {
    lp == null && it != null -> FrameLayout.LayoutParams(it)
    else -> lp ?: FrameLayout.LayoutParams(width.int, height.int)
  }.apply(block)
}

/**
 * 返回 View 的或创建新的 [LinearLayout.LayoutParams]
 * @param width View 的宽度
 * @param height View 的高度
 */
inline fun View.LinearLayoutParams(
  width: Number = wrapContent,
  height: Number = wrapContent,
  block: LinearLayout.LayoutParams.() -> Unit = {}
): LinearLayout.LayoutParams = layoutParams.let {
  val lp = linearLayoutParamsOrNull
  when {
    lp == null && it != null -> LinearLayout.LayoutParams(it)
    else -> lp ?: LinearLayout.LayoutParams(width.int, height.int)
  }.apply(block)
}

/** 返回 View 的 [L] 类型的布局参数 */
inline fun <reified L : ViewGroup.LayoutParams> View.layoutParamsAs(block: L.() -> Unit = {}): L =
  (layoutParams as? L ?: throw TypeCastException(
    "布局参数类型出错，${layoutParams.javaClass.name} 无法转换为 ${L::class.java}"
  )).apply(block)

/** 将当前的 [ViewGroup.LayoutParams] 数据内容复制到 [target] 内 */
fun ViewGroup.LayoutParams.copyInto(target: ViewGroup.LayoutParams?) {
  if (target == null) return
  target.width = width
  target.height = height
  if (this is ViewGroup.MarginLayoutParams && target is ViewGroup.MarginLayoutParams) {
    target.leftMargin = leftMargin
    target.topMargin = topMargin
    target.rightMargin = rightMargin
    target.bottomMargin = bottomMargin
    target.layoutAnimationParameters = layoutAnimationParameters
    target.layoutDirection = layoutDirection
  }
  when {
    this is LinearLayout.LayoutParams && target is LinearLayout.LayoutParams -> {
      target.gravity = gravity
      target.weight = weight
    }
    this is FrameLayout.LayoutParams && target is FrameLayout.LayoutParams -> {
      target.gravity = gravity
    }
  }
}