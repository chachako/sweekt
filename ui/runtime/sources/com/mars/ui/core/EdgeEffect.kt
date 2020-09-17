@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.ui.core

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.*
import com.mars.ui.util.dampedScroll
import kotlin.reflect.KMutableProperty0

/*
 * author: 凛
 * date: 2020/8/21 8:18 PM
 * github: https://github.com/oh-Rin
 * description: 让视图边缘具有与 iOS 类似的效果
 * fork: https://github.com/LawnchairLauncher/Lawnchair/blob/alpha/lawnchair/src/ch/deletescape/lawnchair/views/SpringRecyclerView.kt
 * fixme: 当拖动不同方向的边缘后再进行某个方向的快速滑动时将会出现卡帧情况，原因未知
 */
class SpringEdgeEffect(
  context: Context,
  private val totalSize: () -> Int,
  private val target: KMutableProperty0<Float>,
  private val activeEdge: KMutableProperty0<SpringEdgeEffect?>,
  private val velocityMultiplier: Float,
) : EdgeEffect(context) {
  private var translate = 0f
  private val spring = SpringAnimation(null, object : FloatPropertyCompat<Any>("value") {
    override fun getValue(instance: Any?): Float = target.get()
    override fun setValue(instance: Any?, value: Float) {
      translate = value
      target.set(value)
    }
  }).setSpring(
    SpringForce(0f)
      .setStiffness(100f)
      .setDampingRatio(0.7f)
  )

  /** 脱离手指且开始惯性自动滚动的回调 */
  override fun onAbsorb(velocity: Int) =
    releaseSpring(velocityMultiplier * velocity)

  /** 到了边缘还继续拉时的回调 */
  override fun onPull(deltaDistance: Float, displacement: Float) {
    spring.cancel()
    activeEdge.set(this)
    translate = dampedScroll(
      distance = deltaDistance * velocityMultiplier * totalSize(),
      total = totalSize(),
      last = translate
    )
    target.set(translate)
  }

  /** 在边缘拉动后松手时的回调 */
  override fun onRelease() =
    releaseSpring(0f)

  override fun draw(canvas: Canvas) = false

  private fun releaseSpring(velocity: Float) {
    spring.apply {
      cancel()
      setStartVelocity(velocity)
      setStartValue(target.get())
      start()
    }
  }

  class Manager {
    var view: View? = null
    var shiftX = 0f
      set(value) {
        if (field != value) {
          field = value
          view?.invalidate()
        }
      }

    var shiftY = 0f
      set(value) {
        if (field != value) {
          field = value
          view?.invalidate()
        }
      }

    var activeEdgeX: SpringEdgeEffect? = null
      set(value) {
        if (field != value) {
          field?.run { value?.translate = translate }
        }
        field = value
      }

    var activeEdgeY: SpringEdgeEffect? = null
      set(value) {
        if (field != value) {
          field?.run { value?.translate = translate }
        }
        field = value
      }

    inline fun withSpring(
      canvas: Canvas,
      reverse: Boolean = false,
      body: () -> Unit
    ) {
      if (reverse) {
        canvas.translate(-shiftX, -shiftY)
        body()
        canvas.translate(+shiftX, +shiftY)
      } else {
        canvas.translate(+shiftX, +shiftY)
        body()
        canvas.translate(-shiftX, -shiftY)
      }
    }

    fun createFactory() = SpringEdgeEffectFactory()

    fun createEdgeEffect(view: View, direction: Int): EdgeEffect? = run {
      this.view = view
      when (direction) {
        DIRECTION_LEFT -> SpringEdgeEffect(
          view.context,
          view::getWidth,
          ::shiftX,
          ::activeEdgeX,
          0.34f
        )
        DIRECTION_TOP -> SpringEdgeEffect(
          view.context,
          view::getHeight,
          ::shiftY,
          ::activeEdgeY,
          0.34f
        )
        DIRECTION_RIGHT -> SpringEdgeEffect(
          view.context,
          view::getWidth,
          ::shiftX,
          ::activeEdgeX,
          -0.34f
        )
        DIRECTION_BOTTOM -> SpringEdgeEffect(
          view.context,
          view::getHeight,
          ::shiftY,
          ::activeEdgeY,
          -0.34f
        )
        else -> null
      }
    }

    inner class SpringEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {
      override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect =
        createEdgeEffect(view = recyclerView, direction = direction) ?: super.createEdgeEffect(
          recyclerView,
          direction
        )
    }
  }
}