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

@file:Suppress("RedundantCompanionReference")

package com.meowbase.ui.widget.modifier

import android.graphics.*
import android.renderscript.RenderScript
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.core.view.ancestors
import com.meowbase.toolkit.graphics.drawBitmap
import com.meowbase.toolkit.graphics.padding
import com.meowbase.toolkit.int
import com.meowbase.toolkit.lerp
import com.meowbase.toolkit.view.idOrNew
import com.meowbase.toolkit.view.toBitmap
import com.meowbase.ui.R
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.decoupling.LayoutCanvasProvider
import com.meowbase.ui.core.decoupling.onDrawChildBefore
import com.meowbase.ui.core.graphics.*
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.unit.*
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

/**
 * 为视图添加 Material Design 规范的阴影
 *
 * @param size 阴影大小
 * @see Modifier.shadow 更自由且高级的阴影效果
 */
fun Modifier.elevation(size: SizeUnit) = +object : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    elevation = size.toPx()
  }
}

/**
 * 为视图添加阴影效果
 * NOTE: 与 [Sketch](https://www.sketch.com/) 效果相似
 *
 * @param color 指定阴影颜色，如果不指定则会变成 [diffuseShadow]
 * @param x 横轴偏移大小
 * @param y 纵轴偏移大小
 * @param spread 扩散阴影大小（正值为扩大，负值为缩小）
 * @param blur 模糊程度
 * @param alpha 阴影透明度
 *
 * @see diffuseShadow
 */
fun Modifier.shadow(
  color: Color = Color.Gray,
  x: SizeUnit = SizeUnit.Unspecified,
  y: SizeUnit = SizeUnit.Unspecified,
  spread: SizeUnit = SizeUnit.Unspecified,
  @FloatRange(from = .0, to = 1.0) blur: Float = Shadow.DefaultBlur,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 0.5f,
) = +ShadowModifier(
  Shadow(
    color = color,
    blur = blur,
    y = y,
    x = x,
    spread = spread,
    alpha = alpha
  )
)

/**
 * 为视图添加漂亮的 "弥散阴影" 效果
 * 这会在视图下发生成一个镜像投影，并以模糊的形式呈现
 *
 * @param x 横轴偏移大小
 * @param y 纵轴偏移大小
 * @param spread 扩散阴影大小（正值为扩大，负值为缩小）
 * @param blur 模糊程度
 * @param alpha 阴影透明度
 *
 * @see shadow
 */
fun Modifier.diffuseShadow(
  x: SizeUnit = SizeUnit.Unspecified,
  y: SizeUnit = 54.dp,
  spread: SizeUnit = (-46).dp,
  @FloatRange(from = .0, to = 1.0) blur: Float = 0.18f,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 0.5f,
) = +ShadowModifier(
  Shadow(
    color = Color.Unspecified,
    blur = blur,
    y = y,
    x = x,
    spread = spread,
    alpha = alpha
  )
)


/** 实现为视图添加阴影 */
private class ShadowModifier(val data: Shadow) : Modifier, UpdatableModifier {
  /** 渲染并返回阴影位图 */
  var shadowBitmap: Bitmap? = null

  /**
   * 得出实际模糊值
   * NOTE: [RenderScript] 的模糊值只能在大于 0F 且小于或等于 25 之间
   */
  val blurRadius = lerp(0.001f, 25f, data.blur)

  /**
   * 得出实际缩放值
   * NOTE: 实际上 [blurRadius] 最大的模糊值并不足以满足需求
   * 所以通过计算出较优的数值来作为模糊前位图的缩放大小
   */
  val blurSampling = lerp(100f, 0.001f, data.blur)

  /**
   * 阴影无论如何都要比 View 大上一些
   * 所以这里我们将模糊半径 * 2 来作为阴影的初始大小
   */
  val size = (blurRadius * 2).int

  val spread = data.spread.useOrElse { Px.Zero }.toIntPx()
  val xOffset = data.x.useOrElse { Px.Zero }.toIntPx()
  val yOffset = data.y.useOrElse { 2.dp }.toIntPx()
  val paint = Paint().apply {
    alpha = (data.alpha * 255).roundToInt()
    data.color.useOrNull()?.argb?.also { argb ->
      colorFilter = PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_IN)
    }
  }

  override fun View.realize(parent: ViewGroup?) {
    (parent as? LayoutCanvasProvider)?.onDrawChildBefore { child, _ ->
      if (child == this@realize) render(child)
    }
  }

  override fun View.update(parent: ViewGroup?) {
    ShadowCache.remove(allowedKey)
  }

  fun Canvas.render(view: View) {
    if (shadowBitmap == null) {
      val cacheKey = view.allowedKey
      val cached = ShadowCache[cacheKey]
      when {
        // 只有模糊半径不变，且没有使用 'Diffuse Shadow' 效果时才复用阴影缓存
        cached?.blur == data.blur && cached.color.isSpecified && cached.color == data.color -> {
          shadowBitmap = cached.bitmap
        }
        // 否则使用新的阴影
        else -> {
          // 渲染阴影位图
          shadowBitmap = BlurRender().blurOnce(
            context = view.context,
            // 做一个 padding 避免模糊后的图像超出位图大小
            source = view.toBitmap(blurSampling * 0.01f).padding(blurRadius),
            radius = blurRadius
          )

          // 缓存
          ShadowCache[cacheKey] = data.apply {
            bitmap = shadowBitmap
          }
        }
      }
    }

    val translationZ = view.translationZ.int

    drawBitmap(
      bitmap = shadowBitmap!!,
      outRect = Rect(
        view.left - size + xOffset - spread,
        view.top - size + yOffset - spread + translationZ,
        view.right + size + xOffset + spread,
        view.bottom + size + yOffset + spread + translationZ,
      ),
      paint = paint
    )
  }


  /**
   * 阴影缓存管理
   * TODO 添加圆形、对称直角矩形的默认阴影缓存
   *
   * Strategy:
   * 1.当视图的 [Shape]/[Path] 相同时，允许重用缓存
   * 2.当所有父辈视图以及当前视图的 id 相同时，允许重用缓存（这应该是在列表视图中）
   *
   * @see Modifier.clip
   * @see ShadowCache.allowedKey
   */
  private companion object ShadowCache {
    var cached: WeakReference<MutableMap<Any, Shadow>>? = null

    /**
     * 返回有效的 Key
     *
     * Strategy:
     * 如果这个视图是用 Shape/Path 裁剪过的，则可以用 [R.id.cropped_outline] 的裁剪数据作为 key
     * 否则直接以所有父辈视图的 id 来作为 key - [View.ancestors]
     */
    val View.allowedKey: Any
      get() = getTag(R.id.cropped_outline)?.run { R.id.cropped_outline }
        ?: ancestors.mapNotNull { (it as? View)?.idOrNew }.joinToString() + idOrNew

    operator fun get(key: Any): Shadow? =
      cached?.get()?.get(key)

    operator fun set(key: Any, shadow: Shadow) {
      if (cached == null) cached = WeakReference(mutableMapOf())
      cached?.get()?.set(key, shadow)
    }

    fun remove(key: Any) {
      cached?.get()?.remove(key)
    }
  }
}