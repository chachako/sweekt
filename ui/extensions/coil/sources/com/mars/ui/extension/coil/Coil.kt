@file:Suppress(
  "FunctionName", "SpellCheckingInspection",
  "EXPERIMENTAL_API_USAGE", "NAME_SHADOWING"
)

package com.mars.ui.extension.coil

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.OriginalSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import coil.transition.Transition
import com.mars.ui.Ui
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier
import com.mars.ui.extension.coil.preview.defaultImageLoader
import com.mars.ui.foundation.Image
import com.mars.ui.foundation.With
import okhttp3.HttpUrl
import java.io.File

/**
 * 将 Coil 作为 UiKit 的一部分，其类型为 [Image]
 * [Coil](https://github.com/coil-kt/coil)
 *
 * @author 凛
 * @date 2020/9/14 上午2:23
 * @github https://github.com/oh-Rin
 *
 * @param data 加载图片数据，支持的类型有:
 * - [String] (mapped to a [Uri])
 * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
 * - [HttpUrl]
 * - [File]
 * - [DrawableRes]
 * - [Drawable]
 * - [Bitmap]
 * @param lazy 决定是否立刻进行图片加载请求
 * @param modifier Coil 视图的其他调整
 * @param loader [ImageLoader.Builder]
 * @param scale 图片的缩放类型
 * @param size 源图片加载后的大小，注意：这并不是 View 的大小
 * @param transformation 单个图片转换器
 * @param transformations 多个对源图片进行显示转换的转换器
 * @param transition 图片加载到 View 上的过渡动画
 * @param placeholder 当图片加载请求开始时的占位显示
 * @param error 当图片加载失败时的显示
 * @param fallback 当 [data] 为空时的显示
 * @param memoryCache [ImageRequest.Builder.memoryCachePolicy]
 * @param diskCache [ImageRequest.Builder.diskCachePolicy]
 * @param networkCache [ImageRequest.Builder.networkCachePolicy]
 * @param allowHardware [ImageRequest.Builder.allowHardware]
 * @param allowRgb565 [ImageRequest.Builder.allowRgb565]
 * @param alpha 修改图片的透明度
 *
 * @see CoilImage.update
 */
fun Ui.CoilImage(
  data: Any? = null,
  lazy: Boolean = false,
  modifier: Modifier = Modifier,
  loader: ImageLoader = this.asLayout.context.defaultImageLoader,
  scale: Scale = Scale.FIT,
  size: Size = OriginalSize,
  transformation: Transformation? = null,
  transformations: List<Transformation>? = null,
  transition: Transition = Transition.NONE,
  placeholder: Drawable? = null,
  error: Drawable? = null,
  fallback: Drawable? = null,
  memoryCache: CachePolicy = CachePolicy.ENABLED,
  diskCache: CachePolicy = CachePolicy.ENABLED,
  networkCache: CachePolicy = CachePolicy.ENABLED,
  allowHardware: Boolean = true,
  allowRgb565: Boolean = false,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
): CoilImage = With({ CoilImage(context = asLayout.context) }) {
  it.update(
    withLoad = !lazy,
    data = data,
    modifier = modifier,
    loader = loader,
    scale = scale,
    size = size,
    transformation = transformation,
    transformations = transformations,
    transition = transition,
    placeholder = placeholder,
    error = error,
    fallback = fallback,
    memoryCache = memoryCache,
    diskCache = diskCache,
    networkCache = networkCache,
    allowHardware = allowHardware,
    allowRgb565 = allowRgb565,
    alpha = alpha
  )
}


/**
 * 将 Coil 作为 UiKit 的一部分，其类型为 [Image]
 * [Coil](https://github.com/coil-kt/coil)
 *
 * @author 凛
 * @date 2020/9/15 下午2:07
 * @github https://github.com/oh-Rin
 *
 * @param data 加载图片数据，支持的类型有:
 * - [String] (mapped to a [Uri])
 * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
 * - [HttpUrl]
 * - [File]
 * - [DrawableRes]
 * - [Drawable]
 * - [Bitmap]
 * @param lazy 决定是否立刻进行图片加载请求
 * @param modifier Coil 视图的其他调整
 * @param loader [ImageLoader.Builder]
 * @param alpha 修改图片的透明度
 * @param builder [ImageRequest] 请求前的完整选项
 *
 * @see ImageView.load
 */
inline fun Ui.CoilImage(
  data: Any?,
  lazy: Boolean = false,
  modifier: Modifier = Modifier,
  loader: ImageLoader = this.asLayout.context.defaultImageLoader,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
  builder: ImageRequest.Builder.() -> Unit,
): CoilImage = With({ CoilImage(context = asLayout.context) }) {
  it.update(
    withLoad = !lazy,
    data = data,
    modifier = modifier,
    loader = loader,
    alpha = alpha,
    builder = builder
  )
}