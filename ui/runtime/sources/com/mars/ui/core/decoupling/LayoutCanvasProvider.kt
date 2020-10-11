package com.mars.ui.core.decoupling

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup

/*
 * author: 凛
 * date: 2020/9/27 下午5:47
 * github: https://github.com/oh-Rin
 * description: 提供可修改的 ViewGroup 画布
 */
interface LayoutCanvasProvider {
  var beforeDrawChildCallbacks: ArrayDeque<DrawChildEventWithValue>?
  var afterDrawChildCallbacks: ArrayDeque<DrawChildEvent>?
}

/**
 * 在 [ViewGroup.drawChild] 之前执行 [callback]
 *
 * @param interceptCallSuper 拦截超类调用，这会影响视图后面的 `super.drawChild(canvas, child, drawingTime)`
 * 注意：如果值为 null 则代表不拦截，否则将拦截为传入的 boolean 值 -> `return interceptCallSuper`
 */
inline fun LayoutCanvasProvider.onDrawChildBefore(
  interceptCallSuper: Boolean? = null,
  crossinline callback: Canvas.(child: View, drawingTime: Long) -> Unit
) = beforeDrawChildCallbacks().add { child: View, drawingTime: Long ->
  callback(child, drawingTime)
  interceptCallSuper
}

/**
 * 在 [ViewGroup.drawChild] 之后执行 [callback]
 */
fun LayoutCanvasProvider.onDrawChildAfter(
  callback: Canvas.(child: View, drawingTime: Long) -> Unit
) = afterDrawChildCallbacks().add(callback)


@PublishedApi internal fun LayoutCanvasProvider.beforeDrawChildCallbacks(): ArrayDeque<DrawChildEventWithValue> {
  if (beforeDrawChildCallbacks == null)
    beforeDrawChildCallbacks = ArrayDeque()
  return beforeDrawChildCallbacks!!
}

@PublishedApi internal fun LayoutCanvasProvider.afterDrawChildCallbacks(): ArrayDeque<DrawChildEvent> {
  if (afterDrawChildCallbacks == null)
    afterDrawChildCallbacks = ArrayDeque()
  return afterDrawChildCallbacks!!
}


typealias DrawChildEvent = Canvas.(child: View, drawingTime: Long) -> Unit
typealias DrawChildEventWithValue = Canvas.(child: View, drawingTime: Long) -> Boolean?
