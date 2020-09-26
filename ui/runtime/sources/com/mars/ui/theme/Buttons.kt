package com.mars.ui.theme

import com.mars.ui.Ui
import com.mars.ui.core.Border
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.CircleShape
import com.mars.ui.core.unit.dp
import com.mars.ui.currentTheme
import com.mars.ui.foundation.styles.ButtonStyle


/*
 * author: 凛
 * date: 2020/8/8 3:12 AM
 * github: https://github.com/oh-Rin
 * description: 定义按钮控件的通用样式
 */
class Buttons(
  /** 正常按钮 */
  normal: ButtonStyle = ButtonStyle(),
  /** 图标按钮 */
  icon: ButtonStyle = ButtonStyle(
    shape = CircleShape,
    color = Color.Transparent,
    padding = Padding(all = 14.dp)
  ),
  /** 线框按钮 */
  outlined: ButtonStyle = normal.copy(
    color = Color.Transparent,
    border = Border(size = 1.dp)
  ),
) {
  /**
   * 需要拷贝一份新的按钮样式副本并修改 [Style.id]
   * 以主题系统分辨其他地方的某个控件使用的按钮样式是否为这里的
   */

  val normal = normal.new(0)
  val icon = icon.new(1)
  val outlined = outlined.new(2)

  companion object {
    /**
     * 当应用按钮样式时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断按钮样式备份是否存在
     * 如果存在，根据按钮样式备份的 [Style.id] 判断按钮样式是否为主题按钮样式库中的
     *
     * NOTE: 当 [Style.id] 不为 0 时既代表这不是一个主题样式，不需要更新
     * @return 最后返回主题更新后的按钮样式库的实际按钮样式对象
     */
    internal fun ButtonStyle.resolveButton(ui: Ui): ButtonStyle = when (id) {
      /** 重新获取一遍即可达到更新效果，因为 [currentIcons] 值其实已经变化了 */
      0 -> ui.currentButtons.normal
      1 -> ui.currentButtons.icon
      2 -> ui.currentButtons.outlined
      else -> this // 并非为主题库中的按钮样式，不需要更新
    }
  }
}


/** 返回当前 Ui 主题范围中的按钮样式库 */
@PublishedApi internal inline val Ui.currentButtons: Buttons get() = currentTheme.buttons
