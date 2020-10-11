package com.mars.ui.extension.list.implement

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.mars.ui.core.SpringEdgeEffect

/*
 * author: 凛
 * date: 2020/9/29 上午4:50
 * github: https://github.com/oh-Rin
 * description: 可回收列表的扩展
 */
class RecyclableList @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
  private val springManager = SpringEdgeEffect.Manager()

  init {
    edgeEffectFactory = springManager.createFactory()
  }

  override fun draw(canvas: Canvas) {
    springManager.withSpring(canvas) {
      super.draw(canvas)
    }
  }
}