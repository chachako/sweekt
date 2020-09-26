@file:Suppress("ViewConstructor", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")

package com.mars.ui.skeleton

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mars.toolkit.data.matchParent
import com.mars.toolkit.widget.LayoutParams

/**
 * 一个持有屏幕的实际显示器
 *
 * @author 凛
 * @date 2020/9/23 下午2:06
 * @github https://github.com/oh-Rin
 * @param activity 屏幕附加到的活动
 * @param root 当前活动的根屏幕
 * @param lifecycleOwner 如果为 null 则需要手动调用所有生命周期
 */
class SkeletalEngine(
  private val activity: Activity,
  val root: Skeleton,
  private val lifecycleOwner: LifecycleOwner? = activity as? LifecycleOwner,
) : FrameLayout(activity) {

  init {
    root.engine = this
    root.activityOrNull = activity
    id = Id
    tag = Tag
    layoutParams = LayoutParams(matchParent, matchParent)
    lifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {

    })
  }

  fun dispatchCreate(savedInstanceState: Bundle?) {
    activity.setContentView(this)
    root.onCreate(savedInstanceState)
  }

  fun dispatchStart() {
    root.onAppear()
  }

  fun dispatchStop() {
    root.onDisappear()
  }

  fun dispatchDestroy() {
    root.onDestroy()
  }

  companion object {
    const val Tag = "Monitor"
    val Id = generateViewId()
  }
}

/**
 * 绑定骨骼引擎到 [Activity]
 *
 * @receiver 屏幕附加到的活动
 * @param rootSkeleton 请求一个当前活动的根屏幕
 */
inline fun Activity.engine(rootSkeleton: () -> Skeleton): Lazy<SkeletalEngine> =
  engine(null, rootSkeleton)

/**
 * 绑定骨骼引擎到 [Activity]
 *
 * @receiver 屏幕附加到的活动
 * @param lifecycleOwner 如果为 null 则需要手动调用所有生命周期
 * @param rootSkeleton 请求一个当前活动的根屏幕
 */
inline fun Activity.engine(
  lifecycleOwner: LifecycleOwner? = this as? LifecycleOwner,
  rootSkeleton: () -> Skeleton
): Lazy<SkeletalEngine> {
  val skeleton = rootSkeleton()
  return lazy { SkeletalEngine(this, skeleton, lifecycleOwner) }
}

/**
 * 绑定骨骼引擎到 [Activity]
 *
 * @receiver 屏幕附加到的活动
 * @param rootSkeleton 请求一个当前活动的根屏幕
 * @param lifecycleOwner 如果为 null 则需要手动调用所有生命周期
 */
inline fun Activity.engine(
  rootSkeleton: Skeleton,
  lifecycleOwner: LifecycleOwner? = this as? LifecycleOwner,
): Lazy<SkeletalEngine> = lazy { SkeletalEngine(this, rootSkeleton, lifecycleOwner) }