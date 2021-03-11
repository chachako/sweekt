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

package com.meowbase.toolkit.kotlinpoet.annotation

/**
 * 标记一个类为内部 Api 的一部分
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/11/08 - 14:04
 */
@RequiresOptIn(
  "这是 'toolkit-kotlinpoet' 的一个内部 Api, 你不能从外部调用它。",
  level = RequiresOptIn.Level.ERROR
)
@Retention(value = AnnotationRetention.SOURCE)
internal annotation class InternalApi