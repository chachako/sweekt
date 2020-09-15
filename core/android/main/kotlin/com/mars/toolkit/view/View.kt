@file:Suppress(
  "UsePropertyAccessSyntax", "ConflictingExtensionProperty",
  "UNCHECKED_CAST", "unused"
)

package com.mars.toolkit.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.view.allViews
import androidx.core.view.doOnNextLayout
import androidx.core.view.drawToBitmap
import androidx.core.view.setMargins
import com.mars.toolkit.NoGetter
import com.mars.toolkit.content.asActivity
import com.mars.toolkit.data.Coordinate
import com.mars.toolkit.float
import com.mars.toolkit.int
import com.mars.toolkit.noGetter
import com.mars.toolkit.util.generateViewId
import com.mars.toolkit.widget.LayoutParams
import com.mars.toolkit.widget.MarginLayoutParams
import kotlin.math.max

/** 安全获取 [View] 中的 [Activity] */
inline val View.activity: Activity get() = context.asActivity

/** 返回 [View] 的截图 */
inline val View.bitmap get() = drawToBitmap()

/** 返回视图的 Id, 如果没有则先使用 [assignId] 创建一个新的 Id */
val View.idOrNew: Int
  get() = id.let { currentId ->
    if (currentId == View.NO_ID) assignId() else currentId
  }

/** 封装了更方便的 [View.getParent] 方法，不再需要手动 cast ViewGroup */
val View.parentView: ViewGroup get() = parent as ViewGroup

/** 返回整个视图树，Same as [ViewGroup.allViews] */
val View.tree: Sequence<View> get() = allViews

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
    layoutParams = margin { setMargins(value) }
  }

var View.horizontalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    layoutParams = margin {
      leftMargin = value
      rightMargin = value
    }
  }

var View.verticalMargin: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) {
    layoutParams = margin {
      topMargin = value
      bottomMargin = value
    }
  }

var View.marginTop: Int
  get() = margin().topMargin
  set(@Px value) {
    layoutParams = margin { topMargin = value }
  }

var View.marginBottom: Int
  get() = margin().bottomMargin
  set(@Px value) {
    layoutParams = margin { bottomMargin = value }
  }

var View.marginLeft: Int
  get() = margin().leftMargin
  set(@Px value) {
    layoutParams = margin { leftMargin = value }
  }

var View.marginRight: Int
  get() = margin().rightMargin
  set(@Px value) {
    layoutParams = margin { rightMargin = value }
  }

var View.marginStart: Int
  get() = margin().marginStart
  set(@Px value) {
    layoutParams = margin { marginStart = value }
  }

var View.marginEnd: Int
  get() = margin().marginEnd
  set(@Px value) {
    layoutParams = margin { marginEnd = value }
  }

var View.padding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(value, value, value, value)

var View.horizontalPadding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(paddingLeft, value, paddingRight, value)

var View.verticalPadding: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(@Px value) = setPadding(value, paddingTop, value, paddingBottom)

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
fun View.translationTo(target: View) = arrange {
  val start = coordinate
  val end = target.coordinate
  translationY = end.yFloat - start.yFloat
  translationX = end.xFloat - start.xFloat
}

/**
 * 将当前视图的上下左右位置对齐到另一个视图
 * @param target 最终 [View] 的显示将会与其相同
 */
fun View.coverTo(target: View) = arrange {
  left = target.left
  top = target.top
  bottom = target.bottom
  right = target.right
}

/**
 * 将当前视图的 xy 轴改为另一个视图
 * @param target 最终 [View] 的 xy 轴将会与其相同
 */
fun View.moveTo(target: View) = arrange {
  x = target.x
  y = target.y
}

/**
 * 执行 [block] 前确保 View 已经至少进行过一次放置
 * @param block 安全的 View 代码块
 */
inline fun <V : View> V.arrange(crossinline block: V.() -> Unit) = this.apply {
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
fun View.clipOutline(block: Outline.(View) -> Unit) {
  clipToOutline = true
  outlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
      outline?.block(this@clipOutline)
    }
  }
}

/**
 * 为 View 设置一个好看的黑色阴影
 *
 * @param shadowValue 阴影大小（半径）
 * @param shadowAlpha 阴影的透明度
 * @param roundRadius 圆角半径，在一些圆角 View 上这会更好
 */
fun View.elevation(shadowValue: Number, shadowAlpha: Number = 0.2f, roundRadius: Number = -1f) {
  elevation = shadowValue.float
  outlineProvider = object : ViewOutlineProvider() {
    @TargetApi(21)
    override fun getOutline(view: View, outline: Outline) {
      if (view.width == 0 || view.height == 0) return
      outline.apply {
        alpha = shadowAlpha.float
        if (roundRadius != -1f) {
          setRoundRect(0, 0, view.width, max(1, view.height), roundRadius.float)
        } else {
          setRect(0, 0, view.width, max(1, view.height))
        }
      }
    }
  }
}

/** 从 [View] 父布局上删除自身 */
fun View.removeFromParent() = parentView.removeView(this)

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

private inline fun View.margin(block: ViewGroup.MarginLayoutParams.() -> Unit = {}): ViewGroup.MarginLayoutParams {
  if (layoutParams == null) error("$id 控件 ${javaClass.name} 没有 LayoutParams, 无法进行任何的 margin 操作")
  return MarginLayoutParams(block = block)
}