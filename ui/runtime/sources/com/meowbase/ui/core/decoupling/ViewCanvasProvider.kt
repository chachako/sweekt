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

package com.meowbase.ui.core.decoupling

import android.graphics.Canvas
import android.view.View

/*
 * author: 凛
 * date: 2020/9/27 下午5:47
 * github: https://github.com/RinOrz
 * description: 提供可修改的 View 画布
 */
interface ViewCanvasProvider {
  var beforeDispatchDrawCallbacks: ArrayDeque<DrawEventWithValue>?
  var afterDispatchDrawCallbacks: ArrayDeque<DrawEvent>?

  var beforeDrawCallbacks: ArrayDeque<DrawEventWithValue>?
  var afterDrawCallbacks: ArrayDeque<DrawEvent>?
}

/**
 * 在 [View.dispatchDraw] 之前执行 [callback]
 *
 * @param interceptCallSuper 拦截超类调用，这会影响视图后面的 `super.dispatchDraw(canvas)`
 */
inline fun ViewCanvasProvider.onDispatchDrawBefore(
  interceptCallSuper: Boolean = false,
  crossinline callback: DrawEvent
) = beforeDispatchDrawCallbacks().add {
  callback()
  interceptCallSuper
}

/**
 * 在 [View.dispatchDraw] 之前执行 [callback]
 */
fun ViewCanvasProvider.onDispatchDrawAfter(callback: DrawEvent) =
  afterDispatchDrawCallbacks().add(callback)


@PublishedApi internal fun ViewCanvasProvider.beforeDispatchDrawCallbacks(): ArrayDeque<DrawEventWithValue> {
  if (beforeDispatchDrawCallbacks == null)
    beforeDispatchDrawCallbacks = ArrayDeque()
  return beforeDispatchDrawCallbacks!!
}

@PublishedApi internal fun ViewCanvasProvider.afterDispatchDrawCallbacks(): ArrayDeque<DrawEvent> {
  if (afterDispatchDrawCallbacks == null)
    afterDispatchDrawCallbacks = ArrayDeque()
  return afterDispatchDrawCallbacks!!
}


/**
 * 在 [View.draw] 之前执行 [callback]
 *
 * @param interceptCallSuper 拦截超类调用，这会影响视图后面的 `super.draw(canvas)`
 */
inline fun ViewCanvasProvider.onDrawBefore(
  interceptCallSuper: Boolean = false,
  crossinline callback: DrawEvent
) = beforeDrawCallbacks().add {
  callback()
  interceptCallSuper
}

/**
 * 在 [View.draw] 之后执行 [callback]
 */
fun ViewCanvasProvider.onDrawAfter(callback: DrawEvent) =
  afterDrawCallbacks().add(callback)


@PublishedApi internal fun ViewCanvasProvider.beforeDrawCallbacks(): ArrayDeque<DrawEventWithValue> {
  if (beforeDrawCallbacks == null)
    beforeDrawCallbacks = ArrayDeque()
  return beforeDrawCallbacks!!
}

@PublishedApi internal fun ViewCanvasProvider.afterDrawCallbacks(): ArrayDeque<DrawEvent> {
  if (afterDrawCallbacks == null)
    afterDrawCallbacks = ArrayDeque()
  return afterDrawCallbacks!!
}

typealias DrawEventWithValue = Canvas.() -> Boolean
typealias DrawEvent = Canvas.() -> Unit