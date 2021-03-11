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

@file:Suppress(
  "UsePropertyAccessSyntax", "ConflictingExtensionProperty",
  "unchecked_cast", "unused"
)

package com.meowbase.toolkit.view

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import androidx.core.view.allViews
import androidx.core.view.doOnNextLayout
import androidx.core.view.setMargins
import com.meowbase.toolkit.*
import com.meowbase.toolkit.content.asActivity
import com.meowbase.toolkit.data.Coordinate
import com.meowbase.toolkit.graphics.createBitmap
import com.meowbase.toolkit.util.generateViewId
import com.meowbase.toolkit.widget.LayoutParams
import com.meowbase.toolkit.widget.MarginLayoutParams


/** 安全获取 [View] 中的 [Activity] */
inline val View.activity: Activity get() = context.asActivity

/** 返回视图的 Id, 如果没有则先使用 [assignId] 创建一个新的 Id */
val View.idOrNew: Int
  get() = id.let { currentId ->
    if (currentId == View.NO_ID) assignId() else currentId
  }

/** 封装了更方便的 [View.getParent] 方法，不再需要手动 cast ViewGroup */
val View.parentView: ViewGroup get() = parent as ViewGroup

/** 返回整个视图树，Same as [ViewGroup.allViews] */
val View.tree: Sequence<View> get() = allViews

/** 设置或返回当前 [View] 是否正在截图 */
var View.isShooting: Boolean
  set(value) = setTag(R.id.shooting, value)
  get() = getTag(R.id.shooting) as? Boolean ?: false

/** 修改 [View] 的高宽度 */
inline var View.size: Number
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(value) {
    layoutParams = LayoutParams {
      height = value.int
      width = value.int
    }
  }

/** 修改或获取 [View] 宽度 */
inline var View.width: Number
  get() = getWidth()
  set(value) {
    layoutParams = LayoutParams { width = value.int }
  }

/** 修改或获取 [View] 高度 */
inline var View.height: Number
  get() = getHeight()
  set(value) {
    layoutParams = LayoutParams { height = value.int }
  }

/** [View.setScaleX] and [View.setScaleY] */
inline var View.scale: Number
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(value) {
    scaleX = value.float
    scaleY = value.float
  }

inline var View.scaleX: Number
  get() = scaleX
  set(value) {
    scaleX = value.float
  }

inline var View.scaleY: Number
  get() = scaleY
  set(value) {
    scaleY = value.float
  }

/** 修改或获取 [View] 的透明度 */
inline var View.alpha: Number
  get() = alpha
  set(value) {
    alpha = value.float
  }

/** 填充 [View] 背景色 */
inline var View.backgroundTint: Int
  get() = backgroundTintList?.defaultColor ?: 0
  set(value) {
    backgroundTintList = ColorStateList.valueOf(value)
  }

/** Same as [View.setBackgroundColor] */
inline var View.backgroundColor: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@ColorInt colorInt) = setBackgroundColor(colorInt)

/** Same as [View.setBackgroundResource] */
inline var View.backgroundResource: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@DrawableRes res) = setBackgroundResource(res)

var View.margin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    layoutParams = MarginLayoutParams { setMargins(value) }
  }

var View.horizontalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    layoutParams = MarginLayoutParams {
      leftMargin = value
      rightMargin = value
    }
  }

var View.verticalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    layoutParams = MarginLayoutParams {
      topMargin = value
      bottomMargin = value
    }
  }

var View.marginTop: Int
  get() = MarginLayoutParams().topMargin
  set(@Px value) {
    layoutParams = MarginLayoutParams { topMargin = value }
  }

var View.marginBottom: Int
  get() = MarginLayoutParams().bottomMargin
  set(@Px value) {
    layoutParams = MarginLayoutParams { bottomMargin = value }
  }

var View.marginLeft: Int
  get() = MarginLayoutParams().leftMargin
  set(@Px value) {
    layoutParams = MarginLayoutParams { leftMargin = value }
  }

var View.marginRight: Int
  get() = MarginLayoutParams().rightMargin
  set(@Px value) {
    layoutParams = MarginLayoutParams { rightMargin = value }
  }

var View.marginStart: Int
  get() = MarginLayoutParams().marginStart
  set(@Px value) {
    layoutParams = MarginLayoutParams { marginStart = value }
  }

var View.marginEnd: Int
  get() = MarginLayoutParams().marginEnd
  set(@Px value) {
    layoutParams = MarginLayoutParams { marginEnd = value }
  }

var View.padding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(value, value, value, value)

var View.horizontalPadding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(value, paddingTop, value, paddingBottom)

var View.verticalPadding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(paddingLeft, value, paddingRight, value)

var View.paddingTop: Int
  get() = getPaddingTop()
  set(@Px value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

var View.paddingBottom: Int
  get() = getPaddingBottom()
  set(@Px value) = setPadding(paddingLeft, paddingTop, paddingRight, value)

var View.paddingLeft: Int
  get() = getPaddingLeft()
  set(@Px value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

var View.paddingRight: Int
  get() = getPaddingRight()
  set(@Px value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

var View.paddingStart: Int
  get() = getPaddingStart()
  set(@Px value) = setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

var View.paddingEnd: Int
  get() = getPaddingEnd()
  set(@Px value) = setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

/**
 * 判断布局方向是否为从左到右，例如在中国的手机显示上
 * @see isRtl
 */
inline val View.isLtr get() = layoutDirection == View.LAYOUT_DIRECTION_LTR

/**
 * 判断布局方向是否为从右到左，例如在阿拉伯的手机显示上
 * @see isLtr
 */
inline val View.isRtl get() = !isLtr

/**
 * 获取 View 在屏幕上的坐标
 * @note 调用前确保绘制完成，否则可能获取不到你想要的坐标
 */
val View.coordinate
  get() = IntArray(2).run {
    getLocationOnScreen(this)
    Coordinate(get(0), get(1))
  }

/**
 * 将当前视图平移到另一个视图
 * @param target 最终 [View] 的 [Coordinate] 将会与其相同
 */
fun View.translationTo(target: View) = arranged {
  val start = coordinate
  val end = target.coordinate
  translationY = end.yFloat - start.yFloat
  translationX = end.xFloat - start.xFloat
}

/**
 * 将当前视图的上下左右位置对齐到另一个视图
 * @param target 最终 [View] 的显示将会与其相同
 */
fun View.coverTo(target: View) = arranged {
  left = target.left
  top = target.top
  bottom = target.bottom
  right = target.right
}

/**
 * 将当前视图的 xy 轴改为另一个视图
 * @param target 最终 [View] 的 xy 轴将会与其相同
 */
fun View.moveTo(target: View) = arranged {
  x = target.x
  y = target.y
}

/**
 * 执行 [block] 前确保 View 已经至少进行过一次放置
 * @param block 安全的 View 代码块
 */
inline fun <V : View> V.arranged(crossinline block: V.() -> Unit) = this.apply {
  if (isLaidOut) {
    block()
  } else {
    doOnNextLayout { block() }
  }
}

/** 重置 View 的坐标偏移量 */
fun View.resetOffset() {
  translationY = 0F
  translationX = 0F
}

/**
 * 返回 [View] 的截图
 * @param scale 1.0 为正常比例，数值越小则图像就会缩放的越小，反之越大
 */
fun View.toBitmap(
  scale: Float = 1.0f,
  config: Bitmap.Config = Bitmap.Config.ARGB_8888,
): Bitmap {
  if (!ViewCompat.isLaidOut(this)) {
    throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
  }
  isShooting = true
  val bitmap = createBitmap(width * scale, height * scale, config).applyCanvas {
    if (scale != 1.0f) {
      setMatrix(Matrix().apply { preScale(scale, scale) })
    }
    translate(-scrollX.toFloat(), -scrollY.toFloat())
    draw(this)
  }
  isShooting = false
  return bitmap
}

/** 显示 View */
fun View.visible() {
  visibility = View.VISIBLE
}

/** 隐藏 View */
fun View.invisible() {
  visibility = View.INVISIBLE
}

/** 完全隐藏 View */
fun View.gone() {
  visibility = View.GONE
}

/** 裁剪 [View] 的轮廓 */
fun View.clipOutline(block: View.(Outline) -> Unit) {
  outlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
      if (view == null || outline == null) return
      view.block(outline)
    }
  }
  clipToOutline = true
}

/** 设置 [View.getElevation] 的轮廓 */
fun View.setOutlineProvider(block: View.(Outline) -> Unit) {
  outlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
      if (view == null || outline == null) return
      view.block(outline)
    }
  }
}

/** 从 [View] 父布局上删除自身 */
fun View?.removeFromParent() {
  this ?: return
  val parentView = parent as? ViewGroup ?: return
  parentView.removeView(this)
}

/** 获取父布局并转换为 [Type] */
fun <Type : View?> View.parent() = parent as Type

/**
 * 注册一个单击事件到视图中
 *
 * @param antiShake 防抖动，避免多次快速点击
 * @param threshold [antiShake] 的阈值，每次点击之间的时间间隔，只有超过间隔才会执行 [action]
 * @param action 点击回调 lambda
 */
fun <V : View> V.onClick(
  antiShake: Boolean = true,
  threshold: Int = 500,
  action: (V) -> Unit
) = setOnClickListener(object : View.OnClickListener {
  // 上次点击时间
  private var lastTime = 0L
  override fun onClick(v: View?) {
    if (antiShake) {
      action(v as V)
      return
    }
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastTime >= threshold) {
      lastTime = currentTime
      // 调用点击方法
      action(v as V)
    }
  }
})

/**
 * 注册一个多击事件到视图中
 *
 * @param count 点击次数，默认为双击
 * @param interval 多次点击之间的时间间隔，只有少于间隔才会执行 [action]
 * @param action 点击回调 lambda
 */
fun <V : View> V.onMultipleClick(
  count: Int = 2,
  interval: Int = 1000,
  action: (V) -> Unit
) = setOnClickListener(object : View.OnClickListener {
  private var clickCount = 0
  private var lastClickTime = 0L
  override fun onClick(v: View?) {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime > interval)) {
      clickCount = 1
      lastClickTime = currentTime
      return
    }

    ++clickCount
    lastClickTime = currentTime

    if (clickCount == count) {
      clickCount = 0
      lastClickTime = 0L
      action(v as V)
    }
  }
})

/**
 * 注册一个单击事件到多个视图中
 *
 * @param views 需要使用点击事件的视图
 * @param antiShake 防抖动，避免多次快速点击
 * @param threshold [antiShake] 的阈值，每次点击之间的时间间隔，只有超过这个时间才会进行点击回调
 * @param callback 点击回调 lambda
 */
fun setClicks(
  vararg views: View,
  antiShake: Boolean = true,
  threshold: Int = 500,
  callback: (View) -> Unit
) = views.forEach { it.onClick(antiShake, threshold, callback) }

/**
 * 注册一个点击事件到多个视图中
 *
 * @param viewsAndCallbacks 需要使用点击事件的视图与点击回调
 * @param antiShake 防抖动，避免多次快速点击
 * @param threshold [antiShake] 的阈值，每次点击之间的时间间隔，只有超过这个时间才会进行点击回调
 */
fun setClicks(
  vararg viewsAndCallbacks: Pair<View, (View) -> Unit>,
  antiShake: Boolean = true,
  threshold: Int = 500
) = viewsAndCallbacks.forEach { it.first.onClick(antiShake, threshold, it.second) }

/**
 * 注册一个长按事件到视图中
 *
 * @param consume 是否消费长按事件，
 * 不消费（false）则会在长按后进行其他设置的点击事件
 * 消费（true）则只会执行长按事件，不会在长按后进行其他设置的点击事件
 * @param action 长按回调 lambda
 */
fun <V : View> V.onLongClick(
  consume: Boolean = true,
  action: (V) -> Unit
) = setOnLongClickListener { action(it as V); consume }

/**
 * 注册一个长按事件到多个视图中
 *
 * @param views 需要使用长按事件的视图
 * @param consume 是否消费长按事件，
 * 不消费（false）则会在长按后进行其他设置的点击事件
 * 消费（true）则只会执行长按事件，不会在长按后进行其他设置的点击事件
 * @param callback 长按回调 lambda
 */
fun setLongClicks(
  vararg views: View,
  consume: Boolean = true,
  callback: (View) -> Unit
) = views.forEach { it.onLongClick(consume, callback) }

/**
 * 注册一个长按事件到多个视图中
 *
 * @param viewsAndCallbacks 需要使用长按事件的视图与长按回调
 * @param consume 是否消费长按事件，
 * 不消费（false）则会在长按后进行其他设置的点击事件
 * 消费（true）则只会执行长按事件，不会在长按后进行其他设置的点击事件
 */
fun setLongClicks(
  vararg viewsAndCallbacks: Pair<View, (View) -> Unit>,
  consume: Boolean = true
) = viewsAndCallbacks.forEach { it.first.onLongClick(consume, it.second) }

/**
 * 将左边视图的点击事件覆盖到右边的视图
 * @see View.callOnClick
 */
infix fun View.copyClickTo(target: View) = target.onClick { callOnClick() }

/**
 * 1. Generates a View id that doesn't collide with AAPT generated ones (`R.id.xxx`),
 * more efficiently than [View.generateViewId] if called on the main thread.
 * 2. Assigns it to the View.
 * 3. Disables instance state saving for increased efficiency.
 * as state can't be restored to a view that has its id changing across Activity restarts.
 * 4. Returns the generated id.
 *
 * Note that unlike [View.generateViewId], this method is backwards compatible, below API 17.
 */
fun View.assignId(): Int = generateViewId().also { generatedId ->
  id = generatedId
  isSaveEnabled = false // New id will be generated, so can't restore saved state.
}