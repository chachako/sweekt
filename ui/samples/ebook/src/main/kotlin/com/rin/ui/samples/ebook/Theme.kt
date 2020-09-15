package com.rin.ui.samples.ebook

import com.mars.ui.Theme
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.material.BlurMaterial
import com.mars.ui.core.graphics.shape.RoundedCornerShape
import com.mars.ui.core.graphics.shape.SmoothCornerShape
import com.mars.ui.core.unit.dp
import com.mars.ui.core.unit.sp
import com.mars.ui.foundation.styles.TextStyle
import com.mars.ui.theme.*


private val LightColors get() = LightColors(
  background = Color.White,
)

private val DarkColors get() = DarkColors(
  background = Color(0xff100B20),
  ripple = Color.White.copy(0.2f),
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

val LightTheme = Theme(
  colors = LightColors,
  shapes = Shapes,
  materials = Materials,
  typography = DarkTypography,
)

val DarkTheme = Theme(
  colors = DarkColors,
  shapes = Shapes,
  materials = Materials,
  typography = DarkTypography,
)