package com.rin.ui.samples.ebook

import com.meowbase.toolkit.graphics.loadTypeface
import com.meowbase.ui.Theme
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.material.BlurMaterial
import com.meowbase.ui.core.graphics.shape.RoundedCornerShape
import com.meowbase.ui.core.graphics.shape.SmoothCornerShape
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.core.unit.sp
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.theme.*
import com.meowbase.ui.widget.style.TextStyle
import com.rin.ui.samples.base.Sample
import com.rin.ui.samples.ebook.data.Fonts
import com.rin.ui.samples.ebook.skeleton.HomeSkeleton

/**
 * Ebook App 示例入口
 * [Design](https://dribbble.com/shots/10682895-FREE-Ebooks-APP)
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/11 - 23:32
 */
class Ebook : Sample {
  override fun onCreate() {
    // 加载字体
    loadTypeface(Fonts.SectraFine)
    loadTypeface(Fonts.Montserrat.Medium)
    loadTypeface(Fonts.Montserrat.Regular)
  }

  private fun loadTypeface(path: String) =
    loadTypeface(name = path, path = "ebook/${path}")

  companion object {
    private val LightColors get() = LightColors(
      background = Color.White,
    )

    private val DarkColors get() = DarkColors(
      background = Color(0xff100B20),
      ripple = Color.White.copy(0.2f),
    )

    private val Materials = Color.White.copy(alpha = 0.1f).let {
      Materials(
        thick = BlurMaterial(radius = 20, overlayColor = it),
        regular = BlurMaterial(radius = 10, overlayColor = it),
        thin = BlurMaterial(radius = 8, overlayColor = it),
      )
    }

    private val Shapes = Shapes(
      small = RoundedCornerShape(7.dp),
      medium = RoundedCornerShape(18.dp),
      large = SmoothCornerShape(30.dp),
    )

    private val DarkTypography = run {
      val h6 = TextStyle(
        color = Color.White,
        fontName = PingFangFont.Bold,
        fontSize = 30.sp
      )
      val subtitle1 = TextStyle(
        color = Color.White.copy(0.7f),
        fontName = Fonts.Montserrat.Medium,
        fontSize = 18.sp
      )
      Typography(
        h6 = h6,
        subtitle1 = subtitle1,
        subtitle2 = TextStyle(
          color = Color.White,
          fontName = Fonts.Montserrat.Medium,
          fontSize = 16.sp
        ),
        body1 = h6.copy(fontSize = 20.sp),
        body2 = subtitle1.copy(fontSize = 14.sp),
        caption = TextStyle(
          color = Color.White.copy(alpha = 0.5f),
          fontName = Fonts.Montserrat.Regular,
          fontSize = 14.sp
        ),
      )
    }


    /** 定义这个 App 示例的全局主题 */
    val LightTheme = Theme(
      colors = LightColors,
      shapes = Shapes,
      materials = Materials,
      typography = DarkTypography,
    )

    /** 定义这个 App 示例的全局主题 */
    val DarkTheme = Theme(
      colors = DarkColors,
      shapes = Shapes,
      materials = Materials,
      typography = DarkTypography,
    )

    /** 定义这个 App 示例的 [Skeleton] 入口 */
    val SkeletonEntry = HomeSkeleton()
  }
}