package com.meowbase.ui.core.graphics

import android.graphics.Matrix as AndroidMatrix
import android.widget.ImageView.ScaleType as AndroidScaleType


/*
 * author: 凛
 * date: 2020/8/11 11:59 AM
 * github: https://github.com/RinOrz
 * description: 图片缩放类型，用于适应图片控件
 */
enum class ScaleType(val real: AndroidScaleType) {
  /**
   * 不作缩放处理，原图超过控件的部分则直接裁剪掉多余的
   * @see Center
   */
  None(AndroidScaleType.MATRIX),

  /**
   * 不作缩放处理，将图片显示在控件中心，超过控件的部分直接裁剪
   * NOTE: 图片大小 = 原始图片大小
   * @see None
   */
  Center(AndroidScaleType.CENTER),

  /**
   * 等比缩小图片并显示在控件中心，缩放结果不会大于控件大小或原始图片大小
   * NOTE: 图片大小 <= 控件大小 && 图片大小 <= 原始图片大小
   * @see Fit
   */
  Inside(AndroidScaleType.CENTER_INSIDE),

  /**
   * 与 [Fill] 不同的是，[Crop] 会保留原图比例，然后缩放至宽高与控件相同并居中显示
   * @see Fill
   */
  Crop(AndroidScaleType.CENTER_CROP),

  /**
   * 将图片拉伸显示到控件上，不保留原图比例，可能会变形
   * @see [AndroidMatrix.ScaleToFit.FILL]
   */
  Fill(AndroidScaleType.FIT_XY),

  /**
   * 等比将原图缩放到与控件的最大尺寸相等后居中显示
   * NOTE: 图片大小 = View大小
   * @see Inside
   * @see [AndroidMatrix.ScaleToFit.CENTER]
   */
  Fit(AndroidScaleType.FIT_CENTER),

  /**
   * 将图片按比例缩放至控件的宽度或者高度（取宽和高的最小值）后在控件的开头显示
   * @see [AndroidMatrix.ScaleToFit.START]
   */
  FitStart(AndroidScaleType.FIT_START),

  /**
   * 将图片按比例缩放至控件的宽度或者高度（取宽和高的最小值）后在控件的结尾显示
   * @see [AndroidMatrix.ScaleToFit.END]
   */
  FitEnd(AndroidScaleType.FIT_END),
}