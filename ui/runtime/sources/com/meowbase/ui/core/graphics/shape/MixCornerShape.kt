/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

//@file:Suppress("FunctionName")
//
//package com.meowbase.ui.core.graphics.shape
//
//import com.google.android.material.shape.ShapeAppearanceModel
//import com.meowbase.ui.theme.Shapes
//import com.meowbase.ui.theme.Shapes.Companion.resolveShape
//
///*
// * author: 凛
// * date: 2020/8/9 12:06 PM
// * github: https://github.com/RinOrz
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