package com.mars.toolkit.content

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.view.ViewGroup
import com.mars.toolkit.data.ActivityProvider
import com.mars.toolkit.data.AppData
import com.mars.toolkit.data.ContextProvider
import com.mars.toolkit.int

/** 将上下文转换为 [Activity] */
val Context.asActivity: Activity
  get() = asActivityOrNull ?: throw TypeCastException("不能将 Context 转换为 Activity, 请确保 ${javaClass.name} 或子类是 android.app.Activity")

/** 将上下文转换为 [Activity] 或 null */
val Context.asActivityOrNull: Activity?
  get() = this as? Activity
    ?: (this as? ContextWrapper)?.baseContext?.asActivityOrNull
    ?: (this as? ContextProvider)?.provideContext()?.asActivityOrNull
    ?: (this as? ActivityProvider)?.provideActivity()?.asActivityOrNull

/** 获得当前上下文 [Activity] 的窗口 [ViewGroup] */
val Context.windowView: ViewGroup get() = asActivity.window.decorView as ViewGroup

/**
 * 获得当前上下文中的绝对显示尺寸
 * [Pair.first] 显示宽度
 * [Pair.first] 显示高度
 */
val Context.displayPixels: Pair<Int, Int>
  get() = resources.displayMetrics.run { widthPixels to heightPixels }

inline val Context.displayWidth: Int get() = displayPixels.first

inline val Context.displayHeight: Int get() = displayPixels.second

inline val Context.statusBarHeight get() = getDimensionSize("status_bar_height")

inline val Context.navigationBarHeight get() = getDimensionSize("navigation_bar_height")

/** 获取此应用上下文 [Context] 上的版本号 */
inline val Context.versionCode: Long get() = AppData(packageName).versionCode

@PublishedApi internal fun Context.getDimensionSize(key: String): Int {
  val result = 0
  try {
    val resourceId: Int = Resources.getSystem().getIdentifier(key, "dimen", "android")
    if (resourceId > 0) {
      val sizeOne = resources.getDimensionPixelSize(resourceId)
      val sizeTwo: Int = Resources.getSystem().getDimensionPixelSize(resourceId)
      return if (sizeTwo >= sizeOne) {
        sizeTwo
      } else {
        val densityOne = resources.displayMetrics.density
        val densityTwo: Float = Resources.getSystem().displayMetrics.density
        val f = sizeOne * densityTwo / densityOne
        (if (f >= 0) f + 0.5f else f - 0.5f).int
      }
    }
  } catch (ignored: Resources.NotFoundException) {
    return 0
  }
  return result
}