package com.mars.toolkit.graphics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import com.mars.toolkit.appContext
import com.mars.toolkit.util.Base64Mode
import com.mars.toolkit.util.base64Encode
import com.mars.toolkit.existOrNewFile
import com.mars.toolkit.int
import com.mars.toolkit.util.base64DecodeBytes
import java.io.ByteArrayOutputStream
import java.io.File

/** 获取 [File] 并转换为 [Bitmap] */
val File.asBitmap: Bitmap get() = getBitmap(absolutePath)

/** 根据给定路径得到 [Bitmap] */
fun getBitmap(path: String): Bitmap = BitmapFactory.decodeFile(path)

fun createBitmap(
  width: Number,
  height: Number,
  config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap = Bitmap.createBitmap(width.int, height.int, config)

/**
 * 将 [Bitmap] 作为文件保存到 App 缓存目录中
 *
 * @param name 图片文件的名字
 * @param quality 压缩后的清晰度，100 为不压缩
 * @return [File] 保存到缓存目录后的位图文件
 */
fun Bitmap.saveToCache(name: String, @IntRange(from = 0, to = 100) quality: Int = 100) =
  appContext.cacheDir.resolve(name).also { save(it, quality) }

/**
 * 将 [Bitmap] 作为文件保存到 App 缓存目录中
 *
 * @param file 缓存目录内的追加文件路径
 * @param quality 压缩后的清晰度，100 为不压缩
 * @return [File] 保存到缓存目录后的位图文件
 */
fun Bitmap.saveToCache(file: File, @IntRange(from = 0, to = 100) quality: Int = 100) =
  appContext.cacheDir.resolve(file).also { save(it, quality) }

/**
 * [Bitmap] 保存为 [File]
 *
 * @param path 保存后的位图路径
 * @param quality 压缩后的清晰度，100 为不压缩
 */
fun Bitmap.save(path: String, @IntRange(from = 0, to = 100) quality: Int = 100) =
  save(File(path), quality)

/**
 * [Bitmap] 保存为 [File]
 *
 * @param file 保存后的位图
 * @param quality 压缩后的清晰度，100 为不压缩
 */
fun Bitmap.save(file: File, @IntRange(from = 0, to = 100) quality: Int = 100) {
  file.existOrNewFile().outputStream().use {
    compress(Bitmap.CompressFormat.PNG, quality, it)
  }
}

/**
 * [Bitmap] 保存为 [File]
 *
 * @param path 保存后的位图路径
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Bitmap.save(path: String, @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f) =
  save(File(path), (quality * 100).int)

/**
 * [Bitmap] 保存为 [File]
 *
 * @param file 保存后的位图
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Bitmap.save(file: File, @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f) =
  save(file, (quality * 100).int)

/**
 * [Bitmap] 转为 Base64 [String]
 *
 * @param width 转换后的位图宽度
 * @param height 转换后的位图高度
 * @param flags [Base64] 编码模式
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Bitmap.toBase64(
  @Px width: Int? = null,
  @Px height: Int? = null,
  @Base64Mode flags: Int = Base64.DEFAULT,
  @IntRange(from = 0, to = 100) quality: Int = 100
): String = ByteArrayOutputStream().use {
  var recycle = false
  val bitmap = when {
    width != null || height != null -> {
      recycle = true
      Bitmap.createScaledBitmap(
        this,
        width ?: this.width,
        height ?: this.height,
        true
      )
    }
    else -> this
  }
  bitmap.compress(Bitmap.CompressFormat.PNG, quality, it)
  it.toByteArray().base64Encode(flags).apply {
    if (recycle) bitmap.recycle()
  }
}

/**
 * [Bitmap] 转为 Base64 [String]
 *
 * @param width 转换后的位图宽度
 * @param height 转换后的位图高度
 * @param flags [Base64] 编码模式
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Bitmap.toBase64(
  @Px width: Int? = null,
  @Px height: Int? = null,
  @Base64Mode flags: Int = Base64.DEFAULT,
  @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f
): String = toBase64(width, height, flags, (quality * 100).int)

/**
 * Base64 转为 [Bitmap]
 *
 * @param flags [Base64] 解码模式
 */
fun String.base64ToBitmap(@Base64Mode flags: Int = Base64.DEFAULT): Bitmap =
  base64DecodeBytes(flags).run { BitmapFactory.decodeByteArray(this, 0, size) }