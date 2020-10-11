@file:Suppress("NAME_SHADOWING")

package com.mars.toolkit.graphics

import android.graphics.*
import android.util.Base64
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.scale
import com.mars.toolkit.appContext
import com.mars.toolkit.existOrNewFile
import com.mars.toolkit.float
import com.mars.toolkit.int
import com.mars.toolkit.util.Base64Mode
import com.mars.toolkit.util.base64DecodeBytes
import com.mars.toolkit.util.base64Encode
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

/**
 * 创建一个新的位图副本，并为其添加一些内边距填充
 *
 * @param left 图像离位图画布的左边距离
 * @param top 图像离位图画布的上边距离
 * @param right 图像离位图画布的右边距离
 * @param bottom 图像离位图画布的下边距离
 */
fun Bitmap.padding(
  left: Number = 0,
  top: Number = 0,
  right: Number = 0,
  bottom: Number = 0
): Bitmap {
  val paint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
  val left = left.int
  val top = top.int
  val bottom = bottom.int
  val right = right.int
  val outWidth = width + left.int + right
  val outHeight = height + top.int + bottom
  return createBitmap(outWidth, outHeight).applyCanvas {
    // clear color from bitmap drawing area
    // this is very important for transparent bitmap borders
    // this will keep bitmap transparency
    drawRect(Rect(left, top, outWidth - right, outHeight - bottom), paint)

    // finally, draw bitmap on canvas
    drawBitmap(
      this@padding, // bitmap
      left.float, // left
      top.float, // top
      Paint()
    )
  }
}

/**
 * 创建一个新的位图副本，并为其添加一些内边距填充
 *
 * @param size 图像离位图画布的四周距离
 */
fun Bitmap.padding(size: Number): Bitmap = padding(size, size, size, size)

/**
 * 创建一个新的位图副本，并为其添加一些内边距填充
 *
 * @param horizontal 图像离位图画布的左右两边的距离
 * @param vertical 图像离位图画布的上下两边的距离
 */
fun Bitmap.padding(horizontal: Number = 0, vertical: Number = 0): Bitmap = padding(
  left = horizontal, right = horizontal,
  top = vertical, bottom = vertical
)

/**
 * 修改位图的尺寸
 * @param width 新的位图宽度
 * @param height 新的位图高度
 */
fun Bitmap.changeSize(width: Number, height: Number, filter: Boolean = true): Bitmap =
  scale(width = width.int, height = height.int, filter)

/**
 * 按比例缩放位图
 * @param x 横向缩放比
 * @param y 纵向缩放比
 */
fun Bitmap.scale(x: Float, y: Float, filter: Boolean = true): Bitmap =
  Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { setScale(x, y) }, filter)

/**
 * 按比例缩放位图
 * @param all 横纵缩放比
 */
fun Bitmap.scale(all: Float, filter: Boolean = true): Bitmap =
  Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { setScale(all, all) }, filter)

/** 旋转位图 */
fun Bitmap.rotate(angle: Float, filter: Boolean = true): Bitmap =
  Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { setRotate(angle) }, filter)

/** 创建一个新的位图与矩阵并执行 [block] */
fun Bitmap.applyMatrix(filter: Boolean = true, block: Matrix.() -> Unit): Bitmap =
  Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply(block), filter)
