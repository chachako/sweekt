@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.meowbase.ui.util

import android.app.Activity
import android.graphics.*
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.meowbase.toolkit.content.windowView
import com.meowbase.toolkit.float
import com.meowbase.toolkit.tryQuietly
import com.meowbase.ui.core.graphics.BlurRender
import kotlin.math.roundToInt


/*
 * author: 凛
 * date: 2020/8/14 12:08 AM
 * github: https://github.com/RinOrz
 * description: 视图模糊效果的具体实现
 */
class BlurHelper(private val effectView: View) : ViewTreeObserver.OnPreDrawListener {
  var radius = 10f
  var overlayColor: Int? = null
  internal var isRendering: Boolean = false
  private var blurringCanvas: Canvas? = null
  private var downscaleFactor = 14
  private val rectSrc = Rect()
  private val rectDst = Rect()
  private val paint = Paint()
  private var differentRoot = false
  var isAttached = false

  /** 模糊后的位图 */
  private var blurredBitmap: Bitmap? = null
  private var screenshot: Bitmap? = null
  private val blurRender: BlurRender = BlurRender()

  /** 当前 [Activity] */
  private val activity: Activity
    get() = (effectView.context as? ContextThemeWrapper)?.baseContext as? Activity
      ?: effectView.context as? Activity
      ?: error("Context 不能转换为 Activity, 无法使用模糊效果")

  /**
   * 确保模糊好的位图已经准备好
   */
  fun prepare() {
    var radius = radius / downscaleFactor
    if (radius > 25) {
      downscaleFactor = (downscaleFactor * radius / 25).roundToInt()
      radius = 25f
    }
    val scaledWidth = 1.coerceAtLeast(effectView.width / downscaleFactor)
    val scaledHeight = 1.coerceAtLeast(effectView.height / downscaleFactor)
    if (blurringCanvas == null || blurredBitmap == null
      || blurredBitmap!!.width != scaledWidth
      || blurredBitmap!!.height != scaledHeight
    ) {
      releaseBitmap()
      screenshot = createBitmap(scaledWidth, scaledHeight).applyCanvas {
        blurringCanvas = this
      }
      blurredBitmap = createBitmap(scaledWidth, scaledHeight)
      blurRender.prepare(
        src = screenshot!!,
        context = effectView.context,
        radius = radius,
      )
    }
  }

  fun blur() {
    val decor = activity.windowView
    val oldBitmap = blurredBitmap
    if (effectView.isShown) {
      prepare()
      val locations = IntArray(2)
      val redrawBitmap = blurredBitmap != oldBitmap
      decor.getLocationOnScreen(locations)
      var x = -locations[0]
      var y = -locations[1]
      effectView.getLocationOnScreen(locations)
      x += locations[0]
      y += locations[1]

      // just erase transparent
      screenshot!!.eraseColor(overlayColor ?: 0 and 0xffffff)
      // 开始渲染
      isRendering = true
      // 保存 canvas 状态
      val savedCount = blurringCanvas!!.save()
      try {
        blurringCanvas!!.scale(
          1f * screenshot!!.width / effectView.width,
          1f * screenshot!!.height / effectView.height
        )
        blurringCanvas!!.translate(
          -x.float,
          -y.float
        )
        // 渲染窗口背景
        decor.background?.draw(blurringCanvas!!)
        // 渲染所有视图
        tryQuietly {
          // FIXME: Software rendering doesn't support hardware bitmaps
          decor.draw(blurringCanvas!!)
        }
      } finally {
        // 渲染成功，恢复 canvas 到上一次保存的状态，丢弃一切更改
        isRendering = false
        blurringCanvas!!.restoreToCount(savedCount)
      }
      // 对位图进行模糊
      blurRender.blur(screenshot!!, blurredBitmap!!)
      // 特殊情况，例如弹窗
      if (redrawBitmap || differentRoot) {
        effectView.invalidate()
      }
    }
  }

  fun drawBlur(canvas: Canvas) {
    // 画模糊位图到视图上
    blurredBitmap?.also { bitmap ->
      rectSrc.right = bitmap.width
      rectSrc.bottom = bitmap.height
      rectDst.right = effectView.width
      rectDst.bottom = effectView.height
      // 添加遮罩图层
      overlayColor?.also { paint.colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.DST_OVER) }
      canvas.drawBitmap(bitmap, rectSrc, rectDst, paint)
    }
  }

  fun attach() {
    if (isAttached) return
    isAttached = true
    val decor = activity.windowView
    decor.viewTreeObserver.addOnPreDrawListener(this)
    differentRoot = decor.rootView != effectView.rootView
    if (differentRoot) {
      decor.postInvalidate()
    }
  }

  fun detach() {
    isAttached = false
    activity.windowView.viewTreeObserver.removeOnPreDrawListener(this)
    blurRender.release()
    releaseBitmap()
  }

  private fun releaseBitmap() {
    screenshot?.recycle()?.apply { screenshot = null }
    blurredBitmap?.recycle()?.apply { blurredBitmap = null }
  }

  override fun onPreDraw(): Boolean {
    blur()
    return true
  }
}
