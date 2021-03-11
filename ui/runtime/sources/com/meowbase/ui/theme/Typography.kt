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

@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.meowbase.ui.theme

import com.meowbase.ui.Ui
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.unit.Px
import com.meowbase.ui.core.unit.px
import com.meowbase.ui.core.unit.sp
import com.meowbase.ui.currentTheme
import com.meowbase.ui.widget.style.TextStyle

/*
 * author: 凛
 * date: 2020/8/8 2:57 AM
 * github: https://github.com/RinOrz
 * description: 定义主题的一般通用字体排版, copy from Jetpack-Compose material/Typography.kt
 * specification: https://material.io/design/typography/the-type-system.html#type-scale
 */
class Typography(
  /** 最大的头部标题 */
  h1: TextStyle = TextStyle(
    fontName = PingFangFont.Light,
    fontSize = 56.sp,
    letterSpacing = (-1.5).px,
  ),
  /** 第二大的头部标题 */
  h2: TextStyle = TextStyle(
    fontName = PingFangFont.Light,
    fontSize = 48.sp,
    letterSpacing = (-0.5).px,
  ),
  /** 第三大的头部标题 */
  h3: TextStyle = TextStyle(
    fontName = PingFangFont.Heavy,
    fontSize = 40.sp,
    letterSpacing = Px.Zero,
  ),
  /** 第四大的头部标题 */
  h4: TextStyle = TextStyle(
    fontName = PingFangFont.Bold,
    fontSize = 34.sp,
    letterSpacing = 0.25.px,
  ),
  /** 第五大的头部标题 */
  h5: TextStyle = TextStyle(
    fontName = PingFangFont.Medium,
    fontSize = 24.sp,
    letterSpacing = Px.Zero,
  ),
  /** 第六大的头部标题 */
  h6: TextStyle = TextStyle(
    fontName = PingFangFont.Medium,
    fontSize = 18.sp,
    letterSpacing = 0.15.px,
  ),
  /** 最大的副标题 */
  subtitle1: TextStyle = TextStyle(
    fontSize = 16.sp,
    letterSpacing = 0.15.px,
  ),
  /** 第二大的副标题 */
  subtitle2: TextStyle = TextStyle(
    fontName = PingFangFont.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.px,
  ),
  /** 最大的 body, 一般用于大量文本 */
  body1: TextStyle = TextStyle(
    fontName = PingFangFont.Regular,
    fontSize = 16.sp,
    letterSpacing = 0.5.px,
  ),
  /** 第二大的 body, 一般用于大量文本 */
  body2: TextStyle = TextStyle(
    fontName = PingFangFont.Regular,
    fontSize = 14.sp,
    letterSpacing = 0.25.px,
  ),
  /** 按钮上的文本 */
  button: TextStyle = TextStyle(
    fontName = PingFangFont.Medium,
    fontSize = 14.sp,
    letterSpacing = 1.25.px,
  ),
  /** 字幕，一般用于注释 */
  caption: TextStyle = TextStyle(
    fontName = PingFangFont.Regular,
    fontSize = 12.sp,
    letterSpacing = 0.4.px,
  ),
) {
  /**
   * 需要拷贝一份新的样式副本并修改 [Color.id]
   * 以主题系统分辨其他地方的某个文本的样式是否为这里的
   */

  val h1: TextStyle = h1.new(0)
  val h2: TextStyle = h2.new(1)
  val h3: TextStyle = h3.new(2)
  val h4: TextStyle = h4.new(3)
  val h5: TextStyle = h5.new(4)
  val h6: TextStyle = h6.new(5)
  val subtitle1: TextStyle = subtitle1.new(6)
  val subtitle2: TextStyle = subtitle2.new(7)
  val body1: TextStyle = body1.new(8)
  val body2: TextStyle = body2.new(9)
  val button: TextStyle = button.new(10)
  val caption: TextStyle = caption.new(11)


  /**
   * 创建一份排版库的副本，以覆盖一些值
   */
  fun copy(
    h1: TextStyle = this.h1,
    h2: TextStyle = this.h2,
    h3: TextStyle = this.h3,
    h4: TextStyle = this.h4,
    h5: TextStyle = this.h5,
    h6: TextStyle = this.h6,
    subtitle1: TextStyle = this.subtitle1,
    subtitle2: TextStyle = this.subtitle2,
    body1: TextStyle = this.body1,
    body2: TextStyle = this.body2,
    button: TextStyle = this.button,
    caption: TextStyle = this.caption,
  ): Typography = Typography(
    h1, h2, h3, h4, h5, h6,
    subtitle1, subtitle2,
    body1, body2,
    button, caption
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Typography

    if (h1 != other.h1) return false
    if (h2 != other.h2) return false
    if (h3 != other.h3) return false
    if (h4 != other.h4) return false
    if (h5 != other.h5) return false
    if (h6 != other.h6) return false
    if (subtitle1 != other.subtitle1) return false
    if (subtitle2 != other.subtitle2) return false
    if (body1 != other.body1) return false
    if (body2 != other.body2) return false
    if (button != other.button) return false
    if (caption != other.caption) return false

    return true
  }

  override fun hashCode(): Int {
    var result = h1.hashCode()
    result = 31 * result + h2.hashCode()
    result = 31 * result + h3.hashCode()
    result = 31 * result + h4.hashCode()
    result = 31 * result + h5.hashCode()
    result = 31 * result + h6.hashCode()
    result = 31 * result + subtitle1.hashCode()
    result = 31 * result + subtitle2.hashCode()
    result = 31 * result + body1.hashCode()
    result = 31 * result + body2.hashCode()
    result = 31 * result + button.hashCode()
    result = 31 * result + caption.hashCode()
    return result
  }

  override fun toString(): String =
    "Typography(h1=$h1, " +
      "h2=$h2, " +
      "h3=$h3, " +
      "h4=$h4, " +
      "h5=$h5, " +
      "h6=$h6, " +
      "subtitle1=$subtitle1, " +
      "subtitle2=$subtitle2, " +
      "body1=$body1, " +
      "body2=$body2, " +
      "button=$button, " +
      "caption=$caption" +
      ")"

  companion object {
    /**
     * 当应用文本样式时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断样式备份是否存在
     * 如果存在，根据样式备份的 [TextStyle.id] 判断样式是否为主题排版库中的样式
     * @return 最后返回主题更新后的排版库的实际样式
     */
    internal fun TextStyle.resolveTypography(ui: Ui): TextStyle? = ui.currentTypography.run {
      when (id) {
        /** 重新获取一遍即可达到更新效果，因为 [currentTypography] 值其实已经变化了 */
        0 -> h1
        1 -> h2
        2 -> h3
        3 -> h4
        4 -> h5
        5 -> h6
        6 -> subtitle1
        7 -> subtitle2
        8 -> body1
        9 -> body2
        10 -> button
        11 -> caption
        else -> this@resolveTypography // 并非为主题库中的排版，不需要更新
      }
    }
  }
}

/** 苹果苹方字体 */
object PingFangFont {
  /** 超细 */
  const val Light = "PingFang-Light.ttf"

  /** 正常 */
  const val Regular = "PingFang-Regular.ttf"

  /** 中规中矩 */
  const val Medium = "PingFang-Medium.ttf"

  /** 粗体 */
  const val Bold = "PingFang-Bold.ttf"

  /** 超粗 */
  const val Heavy = "PingFang-Heavy.ttf"
}


/** 返回当前 Ui 主题范围中的排版库 */
@PublishedApi internal inline val Ui.currentTypography: Typography get() = currentTheme.typography