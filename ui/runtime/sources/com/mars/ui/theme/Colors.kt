@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName")

package com.mars.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mars.ui.Ui
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.currentTheme

/*
 * author: 凛
 * date: 2020/8/8 2:55 AM
 * github: https://github.com/oh-Rin
 * description: 定义主题的一般通用颜色, copy from Jetpack-Compose material/Colors.kt
 * specification: https://material.io/design/color/the-color-system.html#color-theme-creation
 */
@Suppress("SpellCheckingInspection")
class Colors constructor(
  primary: Color,
  primaryVariant: Color,
  secondary: Color,
  secondaryVariant: Color,
  background: Color,
  surface: Color,
  error: Color,
  ripple: Color,
  onPrimary: Color,
  onSecondary: Color,
  onBackground: Color,
  onSurface: Color,
  onError: Color,
  isLight: Boolean,
) {
  /**
   * 不应该直接使用此值，因为主题更新后这个值将会被修改，除非你必须这么做，
   * 否则建议使用 [LiveData.observe] 监听此值的变化，并在回调中手动处理新的 [isLight]
   */
  val isLight: MutableLiveData<Boolean> = MutableLiveData(isLight)

  /**
   * 需要拷贝一份新的颜色副本并修改 [Color.id]
   * 以主题系统分辨其他地方的某个颜色是否为这里的颜色
   */

  val primary: Color = primary.new(id = 0)
  val primaryVariant: Color = primaryVariant.new(id = 1)
  val secondary: Color = secondary.new(id = 2)
  val secondaryVariant: Color = secondaryVariant.new(id = 3)
  val background: Color = background.new(id = 4)
  val surface: Color = surface.new(id = 5)
  val error: Color = error.new(id = 6)
  val ripple: Color = ripple.new(id = 7)
  val onPrimary: Color = onPrimary.new(id = 8)
  val onSecondary: Color = onSecondary.new(id = 9)
  val onBackground: Color = onBackground.new(id = 10)
  val onSurface: Color = onSurface.new(id = 11)
  val onError: Color = onError.new(id = 12)


  /**
   * 合并新的颜色库到当前颜色库上
   */
  fun merge(other: Colors) = apply {
    primary.replaceWith(other.primary.useOrNull())
    primaryVariant.replaceWith(other.primaryVariant.useOrNull())
    secondary.replaceWith(other.secondary.useOrNull())
    secondaryVariant.replaceWith(other.secondaryVariant.useOrNull())
    background.replaceWith(other.background.useOrNull())
    surface.replaceWith(other.surface.useOrNull())
    error.replaceWith(other.error.useOrNull())
    ripple.replaceWith(other.ripple.useOrNull())
    onPrimary.replaceWith(other.onPrimary.useOrNull())
    onSecondary.replaceWith(other.onSecondary.useOrNull())
    onBackground.replaceWith(other.onBackground.useOrNull())
    onSurface.replaceWith(other.onSurface.useOrNull())
    onError.replaceWith(other.onError.useOrNull())
    isLight.value = other.isLight.value
  }

  /**
   * Returns a copy of this Colors, optionally overriding some of the values.
   */
  fun copy(
    primary: Color = this.primary,
    primaryVariant: Color = this.primaryVariant,
    secondary: Color = this.secondary,
    secondaryVariant: Color = this.secondaryVariant,
    background: Color = this.background,
    surface: Color = this.surface,
    error: Color = this.error,
    ripple: Color = this.ripple,
    onPrimary: Color = this.onPrimary,
    onSecondary: Color = this.onSecondary,
    onBackground: Color = this.onBackground,
    onSurface: Color = this.onSurface,
    onError: Color = this.onError,
    isLight: Boolean = this.isLight.value!!,
  ): Colors = Colors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    ripple = ripple,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Colors

    if (primary != other.primary) return false
    if (primaryVariant != other.primaryVariant) return false
    if (secondary != other.secondary) return false
    if (secondaryVariant != other.secondaryVariant) return false
    if (background != other.background) return false
    if (surface != other.surface) return false
    if (error != other.error) return false
    if (ripple != other.ripple) return false
    if (onPrimary != other.onPrimary) return false
    if (onSecondary != other.onSecondary) return false
    if (onBackground != other.onBackground) return false
    if (onSurface != other.onSurface) return false
    if (onError != other.onError) return false
    if (isLight != other.isLight) return false

    return true
  }

  override fun hashCode(): Int {
    var result = primary.hashCode()
    result = 31 * result + primaryVariant.hashCode()
    result = 31 * result + secondary.hashCode()
    result = 31 * result + secondaryVariant.hashCode()
    result = 31 * result + background.hashCode()
    result = 31 * result + surface.hashCode()
    result = 31 * result + error.hashCode()
    result = 31 * result + ripple.hashCode()
    result = 31 * result + onPrimary.hashCode()
    result = 31 * result + onSecondary.hashCode()
    result = 31 * result + onBackground.hashCode()
    result = 31 * result + onSurface.hashCode()
    result = 31 * result + onError.hashCode()
    result = 31 * result + isLight.hashCode()
    return result
  }

  override fun toString(): String =
    "Colors(" +
      "isLight=$isLight, " +
      "primary=$primary, " +
      "primaryVariant=$primaryVariant, " +
      "secondary=$secondary, " +
      "secondaryVariant=$secondaryVariant, " +
      "background=$background, " +
      "surface=$surface, " +
      "error=$error, " +
      "ripple=$ripple, " +
      "onPrimary=$onPrimary, " +
      "onSecondary=$onSecondary, " +
      "onBackground=$onBackground, " +
      "onSurface=$onSurface, " +
      "onError=$onError" +
      ")"

  companion object {
    /**
     * 当应用颜色时都会将颜色备份起来
     * 后续主题更新时，在更新回调中先判断颜色备份是否存在
     * 如果存在，根据颜色备份的 [Color.id] 判断颜色是否为主题颜色库中的色
     * @return 最后返回主题更新后的颜色库的实际颜色值
     */
    fun Color.resolveColor(ui: Ui): Color = ui.currentColors.run {
      when (id) {
        /** 重新获取一遍即可达到更新效果，因为 [currentColors] 值其实已经变化了 */
        0 -> primary
        1 -> primaryVariant
        2 -> secondary
        3 -> secondaryVariant
        4 -> background
        5 -> surface
        6 -> error
        7 -> ripple
        8 -> onPrimary
        9 -> onSecondary
        10 -> onBackground
        11 -> onSurface
        12 -> onError
        else -> this@resolveColor // 并非为主题库中的颜色，不需要更新
      }
    }
  }
}

/**
 * Creates a complete color definition for the
 * [Material color specification](https://material.io/design/color/the-color-system.html#color-theme-creation)
 * using the default light theme values.
 *
 * @see DarkColors
 */
fun LightColors(
  /** 主要颜色 */
  primary: Color = Color(0xFF6200EE),
  /** 比 [primary] 更浅或更深一些 */
  primaryVariant: Color = Color(0xFF3700B3),
  /** 辅助颜色 */
  secondary: Color = Color(0xFF03DAC6),
  /** 比 [secondary] 更浅或更深一些 */
  secondaryVariant: Color = Color(0xFF018786),
  /** 背景色 */
  background: Color = Color.White,
  /** 一些控件的表面颜色 */
  surface: Color = Color.White,
  /** 表示出错状态的颜色 */
  error: Color = Color(0xFFB00020),
  /** 水波纹颜色，一般影响于可按压控件的按下时的涟漪 */
  ripple: Color = Color(0xFF9965F4),
  /** 显示在 [primary] 颜色组件上的元素的颜色 */
  onPrimary: Color = Color.White,
  /** 显示在 [secondary] 颜色组件上的元素的颜色 */
  onSecondary: Color = Color.Black,
  /** 显示在 [background] 颜色组件上的元素的颜色 */
  onBackground: Color = Color.Black,
  /** 显示在 [surface] 颜色组件上的元素的颜色 */
  onSurface: Color = Color.Black,
  /** 显示在 [error] 颜色组件上的元素的颜色 */
  onError: Color = Color.White,
): Colors = Colors(
  primary = primary,
  primaryVariant = primaryVariant,
  secondary = secondary,
  secondaryVariant = secondaryVariant,
  background = background,
  surface = surface,
  error = error,
  ripple = ripple,
  onPrimary = onPrimary,
  onSecondary = onSecondary,
  onBackground = onBackground,
  onSurface = onSurface,
  onError = onError,
  isLight = true
)

/**
 * Creates a complete color definition for the
 * [Material color specification](https://material.io/design/color/the-color-system.html#color-theme-creation)
 * using the default dark theme values.
 *
 * @see LightColors
 */
fun DarkColors(
  /** 主要颜色 */
  primary: Color = Color(0xFFBB86FC),
  /** 比 [primary] 更浅或更深一些 */
  primaryVariant: Color = Color(0xFF3700B3),
  /** 辅助颜色 */
  secondary: Color = Color(0xFF03DAC6),
  /**
   * 比 [secondary] 更浅或更深一些
   * 但注意，一般来说在暗色模式下，[secondary] 和 [secondaryVariant] 应该是相同的
   * 因为需要较高的对比度，所以应该不需要变
   */
  secondaryVariant: Color = secondary,
  /** 背景色 */
  background: Color = Color(0xFF121212),
  /** 一些控件的表面颜色 */
  surface: Color = Color(0xFF121212),
  /** 表示出错状态的颜色 */
  error: Color = Color(0xFFCF6679),
  /** 水波纹颜色，一般影响于可按压控件的按下时的涟漪 */
  ripple: Color = Color(0xFFCFABFD),
  /** 显示在 [primary] 颜色组件上的元素的颜色 */
  onPrimary: Color = Color.Black,
  /** 显示在 [secondary] 颜色组件上的元素的颜色 */
  onSecondary: Color = Color.Black,
  /** 显示在 [background] 颜色组件上的元素的颜色 */
  onBackground: Color = Color.White,
  /** 显示在 [surface] 颜色组件上的元素的颜色 */
  onSurface: Color = Color.White,
  /** 显示在 [error] 颜色组件上的元素的颜色 */
  onError: Color = Color.Black,
): Colors = Colors(
  primary = primary,
  primaryVariant = primaryVariant,
  secondary = secondary,
  secondaryVariant = secondaryVariant,
  background = background,
  surface = surface,
  error = error,
  ripple = ripple,
  onPrimary = onPrimary,
  onSecondary = onSecondary,
  onBackground = onBackground,
  onSurface = onSurface,
  onError = onError,
  isLight = false
)


/** 返回当前 Ui 主题范围中的颜色库 */
@PublishedApi internal inline val Ui.currentColors: Colors get() = currentTheme.colors
