package com.mars.toolkit.graphics

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.mars.toolkit.appContext
import com.mars.toolkit.util.Base64Mode
import java.io.File


/** 获取 [File] 并转换为 [Drawable] */
val File.asDrawable: Drawable get() = getBitmap(absolutePath).toDrawable()

/** 根据给定路径 [filePath] 得到 [Drawable] */
fun getDrawable(filePath: String): Drawable =
  Drawable.createFromPath(filePath) ?: error("无法将路径为: $filePath 的文件转换为 Drawable 实例")

/**
 * 将 [Drawable] 作为图片文件保存到 App 缓存目录中
 *
 * @param name 图片文件的名字
 * @param quality 压缩后的清晰度，100 为不压缩
 * @return [File] 保存到缓存目录后的图片文件
 */
fun Drawable.saveToCache(name: String, @IntRange(from = 0, to = 100) quality: Int = 100) =
  toBitmap().saveToCache(name, quality)

/**
 * 将 [Drawable] 作为图片文件保存到 App 缓存目录中
 *
 * @param file 缓存目录内的追加文件路径
 * @param quality 压缩后的清晰度，100 为不压缩
 * @return [File] 保存到缓存目录后的图片文件
 */
fun Drawable.saveToCache(file: File, @IntRange(from = 0, to = 100) quality: Int = 100) =
  toBitmap().saveToCache(file, quality)

/**
 * [Drawable] 保存为 [File]
 *
 * @param path 保存后的图片路径
 * @param quality 压缩后的清晰度，100 为不压缩
 */
fun Drawable.save(path: String, @IntRange(from = 0, to = 100) quality: Int = 100) =
  toBitmap().save(path, quality)

/**
 * [Drawable] 保存为 [File]
 *
 * @param file 保存后的图片
 * @param quality 压缩后的清晰度，100 为不压缩
 */
fun Drawable.save(file: File, @IntRange(from = 0, to = 100) quality: Int = 100) =
  toBitmap().save(file, quality)

/**
 * [Drawable] 保存为 [File]
 *
 * @param path 保存后的图片路径
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Drawable.save(path: String, @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f) =
  toBitmap().save(path, quality)

/**
 * [Drawable] 保存为 [File]
 *
 * @param file 保存后的图片
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Drawable.save(file: File, @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f) =
  toBitmap().save(file, quality)

/**
 * [Drawable] 转为 Base64 [String]
 *
 * @param width 转换后的图片宽度
 * @param height 转换后的图片高度
 * @param flags [Base64] 编码模式
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Drawable.toBase64(
  @Px width: Int? = null,
  @Px height: Int? = null,
  @Base64Mode flags: Int = Base64.DEFAULT,
  @IntRange(from = 0, to = 100) quality: Int = 100
): String = toBitmap().toBase64(width, height, flags, quality)

/**
 * [Drawable] 转为 Base64 [String]
 *
 * @param width 转换后的图片宽度
 * @param height 转换后的图片高度
 * @param flags [Base64] 编码模式
 * @param quality 压缩后的清晰度，1.0f 为不压缩
 */
fun Drawable.toBase64(
  @Px width: Int? = null,
  @Px height: Int? = null,
  @Base64Mode flags: Int = Base64.DEFAULT,
  @FloatRange(from = .0, to = 1.0) quality: Float = 1.0f
): String = toBitmap().toBase64(width, height, flags, quality)

/** [Bitmap] 转为 [BitmapDrawable] */
fun Bitmap.toDrawable(resources: Resources = appContext.resources): Drawable =
  BitmapDrawable(resources, this)

/**
 * Base64 转为 [Bitmap]
 *
 * @param flags [Base64] 解码模式
 */
fun String.base64ToDrawable(
  @Base64Mode flags: Int = Base64.DEFAULT,
  resources: Resources = appContext.resources
): Drawable = base64ToBitmap(flags).toDrawable(resources)