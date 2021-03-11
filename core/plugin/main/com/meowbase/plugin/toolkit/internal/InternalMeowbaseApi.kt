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

@file:Suppress("PackageDirectoryMismatch")

/*
 * author: 凛
 * date: 2020/9/6 下午12:18
 * github: https://github.com/RinOrz
 * description: 标记 Meowbase 内部使用的 api，因为这些特定的 api 只有内部才能使用，不应该公开调用
 */
@RequiresOptIn(message = "这是 Meowbase 内部项目使用的 Api, 此 Api 并不是为了公用而设计的，但也可能只是暂时未公开。")
annotation class InternalMeowbaseApi
