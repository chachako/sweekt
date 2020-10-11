@file:Suppress("Deprecation")

package com.mars.ui.widget.modifier

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import com.mars.toolkit.view.arranged
import com.mars.toolkit.view.isShooting
import com.mars.toolkit.view.setOutlineProvider
import com.mars.ui.R
import com.mars.ui.core.Modifier
import com.mars.ui.core.decoupling.*
import com.mars.ui.core.graphics.NativeOutline
import com.mars.ui.core.graphics.Outline
import com.mars.ui.core.graphics.PorterDuffXfermode
import com.mars.ui.core.graphics.geometry.RectF
import com.mars.ui.core.graphics.shape.*


/**
 * 将视图裁剪为椭圆形
 * @see [EllipseShape]
 */
fun Modifier.clipOval() = clip(OvalShape)

/**
 * 将视图裁剪为直角矩形
 * @see [RectangleCornerShape]
 */
fun Modifier.clipRectangle() = clip(RectangleShape)

/**
 * 将视图裁剪为四个角的弧度都为百分百大小的圆角矩形
 * @see [RoundedCornerShape]
 */
fun Modifier.clipCircleRounded() = clip(CircleRoundedShape)

/**
 * 将视图形状裁剪为指定 [shape]
 *
 * @param antiAlias 当形状需要通过 Canvas 绘制路径情况下是否开启抗锯齿模式
 * 一般情况下开启性能影响不大，在极端情况下建议关闭抗锯齿（事实上锯齿也不会很明显）
 * 圆形、等角矩形，等角圆角矩形则无需设置此属性，因为谷歌已经做了最佳方案
 */
fun Modifier.clip(shape: Shape, antiAlias: Boolean = true) =
  +object : ClipDelegation(shape, antiAlias) {
    override fun View.additional(parent: ViewGroup?) {
      updateOutline { shape.getOutline(rect!!) }
    }
  }


/**
 * 创建路径 [pathBuilder]，并以路径裁剪视图
 *
 * @param antiAlias 路径抗锯齿
 * 一般情况下开启性能影响不大，在极端情况下建议关闭抗锯齿（事实上锯齿也不会很明显）
 */
fun Modifier.clipWithPath(
  antiAlias: Boolean = true,
  pathBuilder: Path.() -> Unit
) = clip(Path().apply(pathBuilder), antiAlias)


/**
 * 根据路径 [path] 裁剪视图
 *
 * @param antiAlias 路径抗锯齿
 * 一般情况下开启性能影响不大，在极端情况下建议关闭抗锯齿（事实上锯齿也不会很明显）
 */
fun Modifier.clip(path: Path, antiAlias: Boolean = true) =
  +object : ClipDelegation(path, antiAlias) {
    override fun View.additional(parent: ViewGroup?) {
      updateOutline {
        Outline().apply {
          this.path = path
        }
      }
    }
  }


/**
 * 根据 [Outline] 数据裁剪视图
 *
 * @param antiAlias 路径抗锯齿
 * 一般情况下开启性能影响不大，在极端情况下建议关闭抗锯齿（事实上锯齿也不会很明显）
 */
fun Modifier.clipOutline(outline: Outline, antiAlias: Boolean = true) =
  +object : ClipDelegation(outline, antiAlias) {
    override fun View.additional(parent: ViewGroup?) {
      updateOutline { outline }
    }
  }


private abstract class ClipDelegation(
  private val clipData: Any,
  private val antiAlias: Boolean
) : Modifier {
  protected var nativeOutline: NativeOutline? = null
  protected var outline: Outline? = null
  protected var rect: RectF? = null
  protected val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

  override fun View.realize(parent: ViewGroup?) {
    if (this !is ViewCanvasProvider) return
    setTag(R.id.cropped_outline, clipData)

    if (antiAlias) {
      antiAliasClip()
    } else {
      fastClip()
    }

    setOutlineProvider {
      nativeOutline?.apply(it::set)
    }

    additional(parent)
  }

  abstract fun View.additional(parent: ViewGroup?)

  fun View.updateOutline(outlineProvider: () -> Outline) {
    arranged {
      rect = RectF(left, top, right, bottom)
      // 更新轮廓对象
      outline = outlineProvider()
      nativeOutline = outline!!.toNativeOutline()
      // 如果 NativeOutline 可以裁剪则直接使用
      if (!outline!!.usePath) {
        clipToOutline = true
        invalidateOutline()
      }
    }
  }

  /**
   * 最佳性能的裁剪方式
   * NOTE: 形状边缘可能会有轻微锯齿
   */
  fun ViewCanvasProvider.fastClip() {
    val draw = drawOutline {
      clipPath(it)
    }
    onDrawAfter(callback = draw)
    if (this is ViewGroup) {
      onDispatchDrawBefore(callback = draw)
    }
  }

  /**
   * 最佳效果的裁剪方式
   * NOTE: 某些重量界面的极端情况下可能会有性能损失，一般情况下基本可以忽视
   * 按需切换 [fastClip]
   */
  fun ViewCanvasProvider.antiAliasClip() {
    var saveCount: Int? = null
    // 在 `super()` 前执行
    val drawBefore = drawOutline {
      saveCount = saveLayer(rect, null, Canvas.ALL_SAVE_FLAG)
    }
    // 在 `super()` 后执行
    val drawAfter = drawOutline {
      paint.xfermode = PorterDuffXfermode.Clear
      it.fillType = Path.FillType.INVERSE_WINDING
      drawPath(it, paint)
      restoreToCount(saveCount!!)
      paint.xfermode = null
    }
    onDrawBefore(callback = drawBefore)
    onDrawAfter(callback = drawAfter)
    if (this is ViewGroup) {
      onDispatchDrawBefore(callback = drawBefore)
      onDispatchDrawAfter(callback = drawAfter)
    }
  }

  fun ViewCanvasProvider.drawOutline(block: Canvas.(path: Path) -> Unit): Canvas.() -> Unit = {
    this@drawOutline as View
    outline?.also {
      it.withClip(this, block)
      // 如果是在 View 正在被截图中，则需要裁剪 NativeOutline, 否则截图后的图像将会是没有裁剪的
      if (isShooting && !it.usePath) {
        block(it.forcePath)
      }
    }
  }
}