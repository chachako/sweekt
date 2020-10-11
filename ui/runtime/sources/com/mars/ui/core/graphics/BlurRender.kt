package com.mars.ui.core.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.graphics.applyCanvas
import com.mars.toolkit.graphics.createBitmap


/*
 * author: 凛
 * date: 2020/8/14 10:48 AM
 * github: https://github.com/oh-Rin
 * description: 位图模糊的具体实现
 * reference: https://github.com/wasabeef/Blurry/blob/master/blurry/src/main/java/jp/wasabeef/blurry/internal/Blur.java
 */
class BlurRender {
  private var renderScript: RenderScript? = null
  private var blurScript: ScriptIntrinsicBlur? = null
  private var blurInput: Allocation? = null
  private var blurOutput: Allocation? = null

  fun prepare(context: Context, src: Bitmap, radius: Float) {
    if (renderScript == null) {
      renderScript = RenderScript.create(context)
      blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    }
    blurScript!!.setRadius(radius)
    blurInput = Allocation.createFromBitmap(
      renderScript, src,
      Allocation.MipmapControl.MIPMAP_NONE,
      Allocation.USAGE_SCRIPT
    )
    blurOutput = Allocation.createTyped(renderScript, blurInput!!.type)
  }

  /**
   * 对 [source] 进行一次模糊
   */
  fun blurOnce(context: Context, source: Bitmap, radius: Float): Bitmap {
    val bitmap = createBitmap(source.width, source.height).applyCanvas {
      val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
      drawBitmap(source, 0f, 0f, paint)
    }
    prepare(context, bitmap, radius)
    blur(bitmap, bitmap)
    release()
    return bitmap
  }

  fun blur(input: Bitmap, output: Bitmap) {
    blurInput!!.copyFrom(input)
    blurScript!!.setInput(blurInput)
    blurScript!!.forEach(blurOutput)
    blurOutput!!.copyTo(output)
  }

  fun release() {
    blurInput?.destroy()?.apply { blurInput = null }
    blurOutput?.destroy()?.apply { blurOutput = null }
    blurScript?.destroy()?.apply { blurScript = null }
    renderScript?.destroy()?.apply { renderScript = null }
  }
}