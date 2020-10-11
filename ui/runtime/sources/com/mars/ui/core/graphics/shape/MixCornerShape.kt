//@file:Suppress("FunctionName")
//
//package com.mars.ui.core.graphics.shape
//
//import com.google.android.material.shape.ShapeAppearanceModel
//import com.mars.ui.theme.Shapes
//import com.mars.ui.theme.Shapes.Companion.resolveShape
//
///*
// * author: 凛
// * date: 2020/8/9 12:06 PM
// * github: https://github.com/oh-Rin
// * description: 多个角可以混合不同的 CornerFamily
// */
//data class MixCornerShape(
//  val topLeft: Corner,
//  val topRight: Corner,
//  val bottomRight: Corner,
//  val bottomLeft: Corner,
//) : Shape {
//  /** [Shapes.resolveShape] */
//  override var id: Int = -1
//  override fun toModelBuilder(): ShapeAppearanceModel.Builder = ShapeAppearanceModel.builder()
//    .setTopLeftCorner(topLeft.family, topLeft.size)
//    .setTopRightCorner(topRight.family, topRight.size)
//    .setBottomRightCorner(bottomRight.family, bottomRight.size)
//    .setBottomLeftCorner(bottomLeft.family, bottomLeft.size)
//
//  /** 创建一个副本并传入给定的 Id 值 */
//  override fun new(id: Int) = copy().also { it.id = id }
//}
//
///** 四个角相同的角形状 */
//fun CornersShape(all: Corner) = MixCornerShape(all, all, all, all)