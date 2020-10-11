@file:Suppress(
  "EXPERIMENTAL_API_USAGE", "NAME_SHADOWING",
  "MemberVisibilityCanBePrivate", "PropertyName", "PropertyName"
)

package com.mars.ui.extension.coil.implement

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.Disposable
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import coil.transition.Transition
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.EraseColorFilter
import com.mars.ui.extension.coil.preview.defaultImageLoader
import com.mars.ui.widget.implement.Image
import okhttp3.HttpUrl
import java.io.File

class CoilImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : Image(context, attrs, defStyleAttr) {
  @PublishedApi internal var loader: ImageLoader = context.defaultImageLoader
  @PublishedApi internal var builder: ImageRequest.Builder? = null
    get() = field ?: ImageRequest.Builder(context)


  /**
   * 更新视图或设置新的图片加载请求
   *
   * @param withLoad 更新视图并且重新请求图片
   * @param data 加载图片数据，支持的类型有:
   * - [String] (mapped to a [Uri])
   * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
   * - [HttpUrl]
   * - [File]
   * - [DrawableRes]
   * - [Drawable]
   * - [Bitmap]
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
   */
  fun update(
    withLoad: Boolean = true,
    data: Any? = null,
    modifier: Modifier = Modifier,
    loader: ImageLoader = this.loader,
    scale: Scale? = null,
    size: Size? = null,
    transformation: Transformation? = null,
    transformations: List<Transformation>? = null,
    transition: Transition? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    fallback: Drawable? = null,
    memoryCache: CachePolicy? = null,
    diskCache: CachePolicy? = null,
    networkCache: CachePolicy? = null,
    allowHardware: Boolean? = null,
    allowRgb565: Boolean? = null,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float? = null,
  ): Disposable? = update(
    withLoad = withLoad,
    data = data,
    modifier = modifier,
    loader = loader,
    alpha = alpha,
  ) {
    scale?.also(::scale)
    size?.also(::size)
    allowHardware?.also(::allowHardware)
    allowRgb565?.also(::allowRgb565)
    transition?.also(::transition)
    memoryCache?.also(::memoryCachePolicy)
    diskCache?.also(::diskCachePolicy)
    networkCache?.also(::networkCachePolicy)
    placeholder?.also(::placeholder)
    error?.also(::error)
    fallback?.also(::fallback)
    var transformations = transformations
    transformation?.also { transformations = transformations.orEmpty() + it }
    transformations?.also(::transformations)
  }


  /**
   * 更新视图或设置新的图片加载请求
   *
   * @param withLoad 更新视图并且重新请求图片
   * @param data 加载图片数据，支持的类型有:
   * - [String] (mapped to a [Uri])
   * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
   * - [HttpUrl]
   * - [File]
   * - [DrawableRes]
   * - [Drawable]
   * - [Bitmap]
   * @param modifier Coil 视图的其他调整
   * @param loader [ImageLoader.Builder]
   * @param alpha 修改图片的透明度
   * @param alpha 修改图片的透明度
   * @param builder [ImageRequest] 请求前的完整选项
   *
   * @see ImageView.load
   */
  inline fun update(
    withLoad: Boolean = true,
    data: Any? = null,
    modifier: Modifier = Modifier,
    loader: ImageLoader = this.loader,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float? = null,
    builder: ImageRequest.Builder.() -> Unit,
  ): Disposable? = let {
    it.loader = loader
    val builder = it.builder!!.apply(builder)
    it.builder = builder
    data?.apply(builder::data)
    super.update(
      modifier = modifier,
      alpha = alpha,
      tint = null,
      tintList = null,
      tintMode = null,
      colorFilter = EraseColorFilter,
      imageResource = null,
      imageBitmap = null,
      imageDrawable = null,
      scaleType = null,
    )
    return@let if (withLoad) load(data) else null
  }

  /** 立即加载图片到当前视图上 */
  fun load(data: Any?): Disposable = loader.enqueue(builder!!.data(data).target(this).build())
}