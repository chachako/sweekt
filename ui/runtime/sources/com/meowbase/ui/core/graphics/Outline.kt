package com.meowbase.ui.core.graphics

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RestrictTo
import androidx.core.graphics.toRect
import com.meowbase.toolkit.float
import com.meowbase.toolkit.graphics.addRect
import com.meowbase.toolkit.graphics.addRoundRect
import com.meowbase.toolkit.int

/*
 * author: 凛
 * date: 2020/9/27 下午3:30
 * github: https://github.com/RinOrz
 * description: 轮廓包装类
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
class Outline {
  var rect: RectF? = null
    private set
  var rectRadius = Float.NEGATIVE_INFINITY
    private set
  var isOval = false
    private set
  var path: Path? = null

  /**
   * 当 [NativeOutline] 无法裁剪轮廓时则返回 true
   * 例如：轮廓为不对称的圆角矩形、直角矩形、圆形或使用路径 [path] 时
   * @see withClip
   */
  val usePath: Boolean get() = path != null

  /**
   * 强制返回路径，即使 [usePath] 为 false
   */
  val forcePath: Path
    get() = path ?: Path().apply {
      if (rect != null) {
        when(rectRadius) {
          0F -> addRect(rect!!)
          else -> addRoundRect(rect!!, rectRadius)
        }
      }
    }

  fun setRoundRect(left: Number, top: Number, right: Number, bottom: Number, radius: Float) {
    this.rect = RectF(left.float, top.float, right.float, bottom.float)
    this.rectRadius = radius
  }

  fun setRect(rect: RectF) {
    this.rect = rect
    this.rectRadius = 0f
  }

  fun setOval(rect: RectF) {
    val left = rect.left
    val right = rect.right
    val top = rect.top
    val bottom = rect.bottom

    if (left >= right || top >= bottom) {
      error("左边位置不能超过右边位置，或者上边位置不能超过下边位置。这不是一个正常的圆形！")
    }

    if (bottom - top == right - left) {
      // represent circle as round rect, for efficiency, and to enable clipping
      setRoundRect(left, top, right, bottom, (bottom - top) / 2.0f)
      return
    }

    path = Path().apply {
      addOval(
        left,
        top,
        right,
        bottom,
        Path.Direction.CW
      )
    }
    rectRadius = Float.NEGATIVE_INFINITY
    isOval = true
  }

  /**
   * 对于非对称的圆角/直角矩形时
   * 需要用 [block] 方式来裁剪形状
   */
  fun withClip(canvas: Canvas, block: Canvas.(path: Path) -> Unit) {
    if (path != null) {
      canvas.block(path!!)
    }
  }

  /** 填充数据并安全的转换为系统轮廓 */
  @Suppress("deprecation") fun toNativeOutline() = NativeOutline().apply {
    when {
      rect != null -> if (rectRadius == 0f) {
        setRect(rect!!.toRect())
      } else {
        setRoundRect(rect!!.toRect(), rectRadius)
      }
      path != null -> when {
        VERSION.SDK_INT >= VERSION_CODES.R -> setPath(path!!)
        VERSION.SDK_INT >= VERSION_CODES.Q || path!!.isConvex -> setConvexPath(path!!)
        else -> setEmpty()
      }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Outline

    if (rect != other.rect) return false
    if (rectRadius != other.rectRadius) return false
    if (path != other.path) return false

    return true
  }

  override fun hashCode(): Int {
    var result = rect?.hashCode() ?: 0
    result = 31 * result + rectRadius.hashCode()
    result = 31 * result + (path?.hashCode() ?: 0)
    return result
  }

}

internal inline fun createOutline(block: Outline.() -> Unit) = Outline().apply(block)

internal inline fun createOutlinePath(block: Path.() -> Unit) = createOutline {
  path = Path().apply(block)
}

internal typealias NativeOutline = android.graphics.Outline