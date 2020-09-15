package com.mars.ui.core

import com.mars.ui.core.graphics.Color
import com.mars.ui.core.unit.SizeUnit

/**
 * 定义边框
 *
 * @author: 凛
 * @date: 2020/8/18 12:43 AM
 * @github: https://github.com/oh-Rin
 * @property size 边框大小
 * @property color 边框颜色
 */
data class Border(val size: SizeUnit = SizeUnit.Unspecified, val color: Color = Color.Unset)