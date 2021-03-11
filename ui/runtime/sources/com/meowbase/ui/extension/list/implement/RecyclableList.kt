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

package com.meowbase.ui.extension.list.implement

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.meowbase.ui.core.SpringEdgeEffect

/*
 * author: 凛
 * date: 2020/9/29 上午4:50
 * github: https://github.com/RinOrz
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