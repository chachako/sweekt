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

package com.meowbase.ui.skeleton.owners

import com.meowbase.ui.skeleton.Skeleton

/**
 * 持有当前的 [Skeleton] 真实实例
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/11 - 11:25
 */
interface SkeletonOwner

internal val SkeletonOwner.current get() = this as Skeleton

internal val SkeletonOwner.stackManager get() = current.system.stackManager