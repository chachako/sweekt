package com.mars.ui.core.graphics.shape

import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.ShapeAppearanceModel

/*
 * author: 凛
 * date: 2020/8/9 12:51 PM
 * github: https://github.com/oh-Rin
 * description: 形状 TODO 应该支持自定义形状
 */
interface Shape {
  /** 用于主题系统分辨此形状是否是主题中的形状，并决定是否可更新 */
  var id: Int

  /** 创建一个形状副本并传入给定的 Id 值 */
  fun new(id: Int): Shape

  /** 转为形状模型构造器 */
  fun toModelBuilder(): ShapeAppearanceModel.Builder
}

/*
 * author: 凛
 * date: 2020/8/8 2:41 PM
 * github: https://github.com/oh-Rin
 * description: 基于角形状
 */
interface CornerBasedShape : Shape {
  val topLeft: CornerSize
  val topRight: CornerSize
  val bottomRight: CornerSize
  val bottomLeft: CornerSize

  val family: Int

  override fun toModelBuilder(): ShapeAppearanceModel.Builder = ShapeAppearanceModel.builder()
    .setTopLeftCorner(family, topLeft)
    .setTopRightCorner(family, topRight)
    .setBottomRightCorner(family, bottomRight)
    .setBottomLeftCorner(family, bottomLeft)
}
