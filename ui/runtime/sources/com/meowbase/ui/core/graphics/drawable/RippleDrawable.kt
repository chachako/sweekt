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

package com.meowbase.ui.core.graphics.drawable

import android.graphics.drawable.Drawable
import com.meowbase.ui.core.graphics.ColorStates

typealias NativeRippleDrawable = android.graphics.drawable.RippleDrawable

/*
 * author: 凛
 * date: 2020/10/2 下午5:25
 * github: https://github.com/RinOrz
 * description: 为 NativeRippleDrawable 提供涟漪色更新能力
 */
class RippleDrawable(val colorStates: ColorStates, content: Drawable?, mask: Drawable?) :
  NativeRippleDrawable(colorStates.toColorStateList(), content, mask)