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

package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi

/**
 * 声明 Kotlinpoet 的真实 [Spec]
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/12 - 00:28
 */
@DslApi
internal interface SpecDeclaration<Spec> {
  /** 返回真实的 Kotlinpoet-Spec */
  fun get(): Spec
}