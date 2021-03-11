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

package com.meowbase.ui

/*
 * author: 凛
 * date: 2020/9/23 下午8:22
 * github: https://github.com/RinOrz
 * description: 代表一个 Ui 主体的代码块，在代码块中可以创建每个部分
 */
typealias UIBody = Ui.() -> Unit

/** 判断 [UIBody] 是否未指定 */
inline val UIBody.isUnspecified: Boolean get() = this == Ui.Unspecified

/** 判断 [UIBody] 是否已经指定 */
inline val UIBody.isSpecified: Boolean get() = this != Ui.Unspecified