package com.mars.ui.core.graphics

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

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