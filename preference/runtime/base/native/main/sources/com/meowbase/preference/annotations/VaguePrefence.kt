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

package com.meowbase.preference.annotations

/*
 * author: 凛
 * date: 2020/9/20 下午12:59
 * github: https://github.com/RinOrz
 * description: 定义 KotprefModel 是可以被混淆的（模糊偏好文件名和偏好属性名）
 */
@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.SOURCE)
annotation class VaguePreference