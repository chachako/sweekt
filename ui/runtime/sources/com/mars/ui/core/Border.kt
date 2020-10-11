package com.mars.ui.core

import android.content.res.ColorStateList
import android.graphics.Paint
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.ColorStates
import com.mars.ui.core.unit.SizeUnit

/**
 * 定义边框信息
 *
 * @author 凛
 * @date 2020/8/18 12:43 AM
 * @github https://github.com/oh-Rin
 * @property size 边框大小
 * @property color 边框颜色
 * @property colorStates 边框带有状态的颜色（将覆盖 [color]）
 * @property ends 末端样式（当渲染边框的是一个没有闭合的路径时使用）
 * @property joins 连接点样式（当渲染边框的是一个多次连接的路径时使用）
 */
data class Border(
  val size: SizeUnit = SizeUnit.Unspecified,
  val color: Color = Color.Unspecified,
  val colorStates: ColorStates? = null,
  val ends: Ends = Ends.None,
  val joins: Joins = Joins.Miter,
) {
  val colorStateList: ColorStateList? = colorStates?.toColorStateList()
  /**
   * 端点
   * [Reference](https://stackoverflow.com/a/5877087)
   */
  enum class Ends(val native: Paint.Cap) {
    /** 无 */
    None(Paint.Cap.BUTT),
    /** 以圆形显示 */
    Round(Paint.Cap.ROUND),
    /** 以方形显示 */
    Square(Paint.Cap.SQUARE),
  }

  /**
   * 连接节点
   * [Reference](https://stackoverflow.com/a/5877087)
   */
  enum class Joins(val native: Paint.Join) {
    /** 交接处以直角连接 */
    Miter(Paint.Join.MITER),
    /** 交接处以圆弧连接 */
    Round(Paint.Join.ROUND),
    /** 交接处以斜角连接 */
    Bevel(Paint.Join.BEVEL),
  }
}