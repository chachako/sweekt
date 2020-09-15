//@file:Suppress("FunctionName", "SpellCheckingInspection", "NAME_SHADOWING")
//
//package com.mars.ui.core
//
//import android.annotation.SuppressLint
//import android.graphics.Canvas
//import android.graphics.Interpolator
//import android.graphics.Rect
//import android.graphics.drawable.Drawable
//import android.os.Handler
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.view.animation.AnimationUtils
//import android.widget.AbsListView
//import androidx.core.os.postDelayed
//import androidx.core.view.ViewCompat
//import com.mars.tools.ktx.createVibration
//import com.mars.ui.core.unit.dp
//import com.mars.ui.core.unit.toIntPx
//import kotlin.math.abs
//import kotlin.math.roundToInt
//
///*
// * author: 凛
// * date: 2020/8/23 2:31 PM
// * github: https://github.com/oh-Rin
// * description: 具有丰富样式的滚动条
// * reference: https://github.com/Mixiaoxiao/FastScroll-Everywhere
// */
//class Scrollbar(
//  val view: View,
//  private var thumb: Drawable,
//  private val width: Int = 3.dp.toIntPx(),
//  private val height: Int = 36.dp.toIntPx(),
//) {
//  private val fingerExtraSpace = 16.dp.toIntPx()
//  private val minHeight = 4.dp
//  private val user get() = view as User
//  private val scrollabilityCache = ScrollabilityCache(view)
//  private var handlingTouchEvent = false
//  private val thumbRect = Rect(0, 0, width, height)
//  private var downY = 0f
//  private var downX = 0f
//  private var fastScrollListener: FastScrollListener? = null
//  private val handler = Handler()
//
//
//  fun dispatchDraw(canvas: Canvas?) {
//    if (canvas != null) drawScrollBars(canvas)
//  }
//
//  fun onAttachedToWindow() {
//    initialAwakenScrollBars()
//  }
//
//  fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
//    event ?: return false
//    return if (event.actionMasked == MotionEvent.ACTION_DOWN) {
//      // Just check if hit the thumb
//      onTouchEvent(event)
//    } else false
//  }
//
//  fun onTouchEvent(event: MotionEvent): Boolean {
//    val y = event.y
//    val x = event.x
//    when (event.actionMasked) {
//      MotionEvent.ACTION_DOWN -> {
//        if (scrollabilityCache.state == ScrollabilityCache.OFF) {
//          handlingTouchEvent = false
//          return false
//        }
//        if (!handlingTouchEvent) {
//          updateThumbRect(0)
//          downY = y
//          downX = x
//          // Check if hit the thumb, Rect.contains(int x ,int y) is NOT
//          // exact
//          if (y >= thumbRect.top - fingerExtraSpace && y <= thumbRect.bottom + fingerExtraSpace && x >= thumbRect.left - fingerExtraSpace && x <= thumbRect.right + fingerExtraSpace) {
//            // 确保手指停止 280 毫秒
//            handler.postDelayed(280) {
//              createVibration(2)
//              handlingTouchEvent = true
//              // try to stop scroll
//              // step 0: call super ACTION_DOWN
//              user.superOnTouchEvent(event)
//              // step 1: call super ACTION_CANCEL
//              val fakeCancelMotionEvent = MotionEvent.obtain(event)
//              fakeCancelMotionEvent.action = MotionEvent.ACTION_CANCEL
//              user.superOnTouchEvent(fakeCancelMotionEvent)
//              fakeCancelMotionEvent.recycle()
//              // update ThumbDrawable state and report
//              // OnFastScrollListener
//              setPressedThumb(true)
//              // Call updateThumbRect to report
//              // OnFastScrollListener.onFastScrolled
//              updateThumbRect(0, true)
//              // Do NOT fade Thumb
//              view.removeCallbacks(scrollabilityCache)
//            }
//          }
//        }
//      }
//      MotionEvent.ACTION_MOVE -> {
//        // 手指移出了滑动条区域则取消长按监听
//        val extra = fingerExtraSpace * 2
//        if (abs(x - downX) > thumbRect.width() + extra || abs(y - downY) > thumbRect.height() + extra) {
//          handler.removeCallbacksAndMessages(null)
//        }
//        if (handlingTouchEvent) {
//          val touchDeltaY = (y - downY).roundToInt()
//          if (touchDeltaY != 0) {
//            updateThumbRect(touchDeltaY)
//            // only touchDeltaY != 0, we save the touchY, to Avoid
//            // accuracy error
//            downY = y
//          }
//        }
//      }
//      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//        handler.removeCallbacksAndMessages(null)
//        if (handlingTouchEvent) {
//          setPressedThumb(false)
//          handlingTouchEvent = false
//          awakenScrollBars()
//        }
//      }
//    }
//    if (handlingTouchEvent) {
//      view.invalidate()
//      view.parent.requestDisallowInterceptTouchEvent(true)
//      return true
//    }
//    return false
//  }
//
//  /**
//   * [View.awakenScrollBars]
//   * @param startDelay 当没有任何活动时延迟多久关闭滑动条
//   */
//  fun awakenScrollBars(startDelay: Long = 1500): Boolean {
//    var startDelay = startDelay
//    ViewCompat.postInvalidateOnAnimation(view)
//    // log("awakenScrollBars call startDelay->" + startDelay);
//    if (!handlingTouchEvent) {
//      if (scrollabilityCache.state == ScrollabilityCache.OFF) {
//        // FIXME: this is copied from WindowManagerService.
//        // We should get this value from the system when it
//        // is possible to do so.
//        startDelay = 750L.coerceAtLeast(startDelay)
//      }
//      // Tell mScrollCache when we should start fading. This may
//      // extend the fade start time if one was already scheduled
//      val fadeStartTime = AnimationUtils.currentAnimationTimeMillis() + startDelay
//      scrollabilityCache.fadeStartTime = fadeStartTime
//      scrollabilityCache.state = ScrollabilityCache.ON
//      // Schedule our fader to run, unscheduling any old ones first
//      // if (mAttachInfo != null) {
//      // mAttachInfo.mHandler.removeCallbacks(scrollCache);
//      // mAttachInfo.mHandler.postAtTime(scrollCache, fadeStartTime);
//      // }
//      view.removeCallbacks(scrollabilityCache)
//      view.postDelayed(
//        scrollabilityCache,
//        fadeStartTime - AnimationUtils.currentAnimationTimeMillis()
//      )
//    }
//    return false
//  }
//
//  // See View.class
//  private fun initialAwakenScrollBars(): Boolean {
//    return awakenScrollBars(scrollabilityCache.scrollBarDefaultDelayBeforeFade * 4.toLong())
//  }
//
//  private fun drawScrollBars(canvas: Canvas) {
//    var invalidate = false
//    if (handlingTouchEvent) {
//      thumb.alpha = 255
//    } else {
//      // Copy from View.class
//      val cache = scrollabilityCache
//      // cache.scrollBar = mThumbDrawable;
//      val state = cache.state
//      if (state == ScrollabilityCache.OFF) {
//        return
//      }
//      if (state == ScrollabilityCache.FADING) {
//        // We're fading -- get our fade interpolation
//        if (cache.interpolatorValues == null) {
//          cache.interpolatorValues = FloatArray(1)
//        }
//        val values = cache.interpolatorValues
//        // Stops the animation if we're done
//        if (cache.scrollBarInterpolator.timeToValues(values) == Interpolator.Result.FREEZE_END) {
//          cache.state = ScrollabilityCache.OFF
//        } else {
//          // in View.class is "cache.scrollBar.mutate()"
//          thumb.alpha = Math.round(values!![0])
//        }
//        invalidate = true
//      } else {
//        // reset alpha, in View.class is "cache.scrollBar.mutate()"
//        thumb.alpha = 255
//      }
//    }
//
//    // Draw the thumb
//    if (updateThumbRect(0)) {
//      val scrollY = view.scrollY
//      val scrollX = view.scrollX
//      thumb.setBounds(
//        thumbRect.left + scrollX, thumbRect.top + scrollY, thumbRect.right + scrollX,
//        thumbRect.bottom + scrollY
//      )
//      thumb.draw(canvas)
//    }
//    if (invalidate) {
//      view.invalidate()
//    }
//  }
//
//  private fun setPressedThumb(pressed: Boolean) {
//    if (pressed) thumb.setBounds(thumb.bounds.left, thumb.bounds.top, thumb.bounds.right + 50, thumb.bounds.bottom)
//
//    thumb.state = if (pressed) intArrayOf(android.R.attr.state_pressed) else (thumb.state + intArrayOf(
//      -android.R.attr.state_pressed
//    ))
//    view.invalidate()
//    if (fastScrollListener != null) {
//      if (pressed) {
//        fastScrollListener!!.fastScrollStart(view)
//      } else {
//        fastScrollListener!!.fastScrollEnd(view)
//      }
//    }
//  }
//
//  private fun updateThumbRect(touchDeltaY: Int): Boolean {
//    return updateThumbRect(touchDeltaY, false)
//  }
//
//  /**
//   * updateThumbRect
//   *
//   * @param touchDeltaY
//   * ,if touchDeltaY != 0, will report
//   * FastScrollListener.onFastScrolled
//   * @param forceReportFastScrolled
//   * , if true, will force report FastScrollListener.onFastScrolled
//   * @return false:Thumb return false means no need to draw thumb
//   */
//  private fun updateThumbRect(touchDeltaY: Int, forceReportFastScrolled: Boolean): Boolean {
//    val thumbWidth = thumbRect.width()
//    thumbRect.right = view.width
//    thumbRect.left = thumbRect.right - thumbWidth
//    val scrollRange = user.superComputeVerticalScrollRange() // 整体的全部高度
//    if (scrollRange <= 0) { // no content, 仅在有内容的时候绘制thumb
//      return false
//    }
//    val scrollOffset = user.superComputeVerticalScrollOffset() // 上方已经滑动出本身范围的高度
//    val scrollExtent = user.superComputeVerticalScrollExtent() // 当前显示区域的高度
//    val scrollMaxOffset = scrollRange - scrollExtent
//    if (scrollMaxOffset <= 0) { // can not scroll, 内容部分不够或刚好充满
//      return false
//    }
//    val scrollPercent = scrollOffset * 1f / scrollMaxOffset
//    val visiblePercent = scrollExtent * 1f / scrollRange
//    // log("scrollPercent->" + scrollPercent + " visiblePercent->" +
//    // visiblePercent);
//    val viewHeight: Int = view.height
//    val thumbHeight = minHeight.toIntPx().coerceAtLeast((visiblePercent * viewHeight).roundToInt())
//    thumbRect.bottom = thumbRect.top + thumbHeight
//    val thumbTop = ((viewHeight - thumbHeight) * scrollPercent).roundToInt()
//    thumbRect.offsetTo(thumbRect.left, thumbTop)
//    if (touchDeltaY != 0) { // compute the ScrollOffset, 按touchDeltaY计算滚动
//      var newThumbTop = thumbTop + touchDeltaY
//      val minThumbTop = 0
//      val maxThumbTop = viewHeight - thumbHeight
//      if (newThumbTop > maxThumbTop) {
//        newThumbTop = maxThumbTop
//      } else if (newThumbTop < minThumbTop) {
//        newThumbTop = minThumbTop
//      }
//      val newScrollPercent = newThumbTop * 1f / maxThumbTop // 百分比
//      val newScrollOffset = ((scrollRange - scrollExtent) * newScrollPercent).roundToInt()
//      val viewScrollDeltaY = newScrollOffset - scrollOffset
//      if (view is AbsListView) {
//        // Call scrollBy to AbsListView , not work correctly
//        view.smoothScrollBy(viewScrollDeltaY, 0)
//      } else {
//        view.scrollBy(0, viewScrollDeltaY)
//      }
//      fastScrollListener?.fastScrolling(
//        view,
//        touchDeltaY,
//        viewScrollDeltaY,
//        newScrollPercent
//      )
//    } else {
//      if (forceReportFastScrolled) {
//        fastScrollListener?.fastScrolling(view, 0, 0, scrollPercent)
//      }
//    }
//    return true
//  }
//
//  /** 标记是一个使用 Scrollbar 的 View */
//  interface User {
//    /** 转接超类的对应方法 */
//    fun superOnTouchEvent(event: MotionEvent): Boolean
//    fun superComputeVerticalScrollExtent(): Int
//    fun superComputeVerticalScrollOffset(): Int
//    fun superComputeVerticalScrollRange(): Int
//  }
//
//  interface FastScrollListener {
//    fun fastScrollStart(view: View?)
//    fun fastScrolling(
//      view: View?, touchDeltaY: Int, viewScrollDeltaY: Int,
//      scrollPercent: Float
//    )
//
//    fun fastScrollEnd(view: View?)
//  }
//
//  /** Copy from View.class */
//  private class ScrollabilityCache(var host: View) : Runnable {
//    val scrollBarDefaultDelayBeforeFade: Int = ViewConfiguration.getScrollDefaultDelay()
//    val scrollBarFadeDuration: Int = ViewConfiguration.getScrollBarFadeDuration()
//
//    // public ScrollBarDrawable scrollBar;
//    // public Drawable scrollBar;
//    var interpolatorValues: FloatArray? = null
//    val scrollBarInterpolator = Interpolator(1, 2)
//
//    /**
//     * When fading should start. This time moves into the future every time
//     * a new scroll happens. Measured based on SystemClock.uptimeMillis()
//     */
//    var fadeStartTime: Long = 0
//
//    /** * The current state of the scrollbars: ON, OFF, or FADING  */
//    var state = OFF
//    override fun run() {
//      val now = AnimationUtils.currentAnimationTimeMillis()
//      if (now >= fadeStartTime) {
//
//        // the animation fades the scrollbars out by changing
//        // the opacity (alpha) from fully opaque to fully
//        // transparent
//        var nextFrame = now.toInt()
//        var framesCount = 0
//        val interpolator = scrollBarInterpolator
//
//        // Start opaque
//        interpolator.setKeyFrame(framesCount++, nextFrame, OPAQUE)
//
//        // End transparent
//        nextFrame += scrollBarFadeDuration
//        interpolator.setKeyFrame(framesCount, nextFrame, TRANSPARENT)
//        state = FADING
//
//        // Kick off the fade animation
//        // host.invalidate(true);
//        host.invalidate()
//      }
//    }
//
//    companion object {
//      /*** Scrollbars are not visible  */
//      const val OFF = 0
//
//      /** * Scrollbars are visible  */
//      const val ON = 1
//
//      /** * Scrollbars are fading away  */
//      const val FADING = 2
//      private val OPAQUE = floatArrayOf(255f)
//      private val TRANSPARENT = floatArrayOf(0.0f)
//    }
//  }
//}