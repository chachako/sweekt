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

package com.meowbase.ui.core.graphics.material

import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.theme.Materials
import com.meowbase.ui.theme.Materials.Companion.resolveMaterial

/*
 * author: 凛
 * date: 2020/8/16 8:49 PM
 * github: https://github.com/RinOrz
 * description: 高斯模糊材质
 */
data class BlurMaterial(
  /** 模糊半径 */
  internal val radius: Number? = null,

  /** 覆盖层颜色, 默认不叠加任何颜色 */
  internal val overlayColor: Color = Color.Unspecified,
) : Material {
  /** [Materials.resolveMaterial] */
  override var id: Int = -1

  /** 将当前 [BlurMaterial] 与 [other] 进行合并后得到一个新的模糊材质 */
  override fun merge(other: Material): BlurMaterial {
    if (other !is BlurMaterial) return this
    return BlurMaterial(
      radius = other.radius ?: this.radius,
      overlayColor = other.overlayColor.useOrElse { this.overlayColor },
    )
  }

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}