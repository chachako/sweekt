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

@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import com.meowbase.ui.Ui
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.style.DividerStyle
import com.meowbase.ui.theme.currentStyles


/** 分频器/分割线 */
fun Ui.Divider(
  /** [DividerStyle.color] */
  color: Color = Color.Unspecified,
  /** [DividerStyle.thickness] */
  thickness: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.startIndent] */
  startIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.endIndent] */
  endIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.indent] */
  indent: SizeUnit? = null,
  /** 分割线的样式 */
  style: DividerStyle = currentStyles.divider,
  /**
   * 分割线的方向
   * NOTE: 这会影响 [thickness] 是高度还是宽度
   * [startIndent] 是上还是左
   * [endIndent] 是右还是下
   */
  orientation: Orientation = Orientation.Horizontal,
): Divider = With(::Divider) {
  it.update(
    color = color,
    thickness = thickness,
    startIndent = startIndent,
    endIndent = endIndent,
    indent = indent,
    style = style,
    orientation = orientation
  )
}

/**
 * 垂直方向的分频器/分割线
 * @see Divider
 */
fun Ui.VerticalDivider(
  /** [DividerStyle.color] */
  color: Color = Color.Unspecified,
  /** [DividerStyle.thickness] */
  thickness: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.startIndent] */
  startIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.endIndent] */
  endIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.indent] */
  indent: SizeUnit? = null,
  /** 分割线的样式 */
  style: DividerStyle = currentStyles.divider,
): Divider = Divider(
  color = color,
  thickness = thickness,
  startIndent = startIndent,
  endIndent = endIndent,
  indent = indent,
  style = style,
  orientation = Orientation.Vertical
)

/**
 * 水平方向的分频器/分割线
 * @see Divider
 */
fun Ui.HorizontalDivider(
  /** [DividerStyle.color] */
  color: Color = Color.Unspecified,
  /** [DividerStyle.thickness] */
  thickness: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.startIndent] */
  startIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.endIndent] */
  endIndent: SizeUnit = SizeUnit.Unspecified,
  /** [DividerStyle.indent] */
  indent: SizeUnit? = null,
  /** 分割线的样式 */
  style: DividerStyle = currentStyles.divider,
): Divider = Divider(
  color = color,
  thickness = thickness,
  startIndent = startIndent,
  endIndent = endIndent,
  indent = indent,
  style = style,
  orientation = Orientation.Horizontal
)