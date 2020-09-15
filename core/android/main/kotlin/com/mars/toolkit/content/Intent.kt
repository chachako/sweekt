package com.mars.toolkit.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mars.toolkit.appContext
import java.io.File
import java.io.Serializable


/**
 * 启动一个 [T] [Activity]
 * @param params Intent 参数, 将使用 [fillIntentArguments] 转换为 [Intent.putExtra]
 * @param configIntent Intent 额外的自定义设置
 */
inline fun <reified T : Activity> Context.start(
  vararg params: Pair<String, Any?>,
  bundle: Bundle? = null,
  configIntent: Intent.() -> Unit = {}
) = createIntent(this, T::class.java, params).apply(configIntent).run {
  bundle?.let { startActivity(this, bundle) } ?: startActivity(this)
}

/**
 * 启动文件管理 Action 进行文件选择
 * @param requestCode Activity 请求码
 * @param multipleChoice 决定是否允许选择并返回多个文件
 */
fun Activity.chooseFile(requestCode: Int, multipleChoice: Boolean = false) {
  val intent = Intent(Intent.ACTION_GET_CONTENT)
  intent.type = "*/*"
  intent.addCategory(Intent.CATEGORY_OPENABLE)
  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multipleChoice)
  startActivityForResult(Intent.createChooser(intent, "Choose File"), requestCode)
}

/**
 * Install apk
 * @note need android.permission.REQUEST_INSTALL_PACKAGES after N
 */
fun File.installApk() = getInstallIntent()?.run { appContext.startActivity(this) }

/**
 * Return the Intent for install apk
 */
fun File.getInstallIntent(): Intent? {
  if (!exists()) return null
  val intent = Intent(Intent.ACTION_VIEW)
  val uri: Uri

  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
    uri = Uri.fromFile(this)
  } else {
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val authority = "${appContext.packageName}.fileprovider"
    uri = FileProvider.getUriForFile(appContext, authority, this)
  }
  intent.setDataAndType(uri, "application/vnd.android.package-archive")
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  return intent
}

inline fun createIntent(
  action: String,
  uri: Uri? = null,
  configIntent: Intent.() -> Unit = {}
) = Intent(action, uri).apply(configIntent)

/**
 * 创建意图
 * @param ctx 要用到此意图的类
 * @param params 意图参数 -> [Intent.putExtra] [fillIntentArguments]
 */
fun <T> createIntent(
  ctx: Context,
  clazz: Class<out T>,
  params: Array<out Pair<String, Any?>> = arrayOf()
): Intent {
  val intent = Intent(ctx, clazz)
  if (params.isNotEmpty()) fillIntentArguments(intent, params)
  return intent
}

/**
 * 转换实际参数
 * [Pair.first] -> 扩展数据名称
 * [Pair.second] -> 扩展数据值
 * @see [Intent.putExtra]
 */
fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
  if (params.isEmpty()) return
  params.forEach {
    when (val value = it.second) {
      is Array<*> -> when {
        value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
        value.isArrayOf<String>() -> intent.putExtra(it.first, value)
        value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
        else -> error("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
      }
      is Int -> intent.putExtra(it.first, value)
      is Long -> intent.putExtra(it.first, value)
      is String -> intent.putExtra(it.first, value)
      is Float -> intent.putExtra(it.first, value)
      is Double -> intent.putExtra(it.first, value)
      is Char -> intent.putExtra(it.first, value)
      is Short -> intent.putExtra(it.first, value)
      is Boolean -> intent.putExtra(it.first, value)
      is Bundle -> intent.putExtra(it.first, value)
      is IntArray -> intent.putExtra(it.first, value)
      is LongArray -> intent.putExtra(it.first, value)
      is FloatArray -> intent.putExtra(it.first, value)
      is DoubleArray -> intent.putExtra(it.first, value)
      is CharArray -> intent.putExtra(it.first, value)
      is ShortArray -> intent.putExtra(it.first, value)
      is BooleanArray -> intent.putExtra(it.first, value)
      is CharSequence -> intent.putExtra(it.first, value)
      is Serializable -> intent.putExtra(it.first, value)
      is Parcelable -> intent.putExtra(it.first, value)
      null -> intent.putExtra(it.first, null as Serializable?)
      else -> error("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
    }
    return@forEach
  }
}

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
  createIntent(this, T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
  createIntent(
    activity!!,
    T::class.java,
    params
  )

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.clearWhenTaskReset(): Intent =
  apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.newDocument(): Intent = apply {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
  } else {
    @Suppress("DEPRECATION")
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
  }
}

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.excludeFromRecents(): Intent =
  apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }