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

package com.meowbase.plugin.toolkit.hooker

import com.meowbase.toolkit.kotlinClass
import org.gradle.api.Project
import kotlin.reflect.KClass

/*
 * author: 凛
 * date: 2020/9/17 下午9:39
 * github: https://github.com/RinOrz
 * description: 收集一切注册过的 Hooker
 */
@PublishedApi internal val collected = mutableListOf<KClass<out TaskHooker>>()

/**
 * 将多个 Hooker 注册到 [collected]
 * NOTE: 注册后将会立刻进行挂钩
 */
fun Project.registerHooks(vararg hookers: KClass<out TaskHooker>) = afterEvaluate {
  hookers.forEach { hooker ->
    if (!collected.contains(hooker)) {
      hooker.java.getConstructor().newInstance().also {
        it.project = this
        it.hook()
      }
      collected.add(hooker)
    }
  }
}

/**
 * 将多个 Hooker 注册到 [collected]
 * NOTE: 注册后将会立刻进行挂钩
 */
fun Project.registerHooks(vararg hookers: TaskHooker) = afterEvaluate {
  hookers.forEach { hooker ->
    if (!collected.contains(hooker.kotlinClass)) {
      hooker.also {
        it.project = this
        it.hook()
      }
      collected.add(hooker.kotlinClass)
    }
  }
}