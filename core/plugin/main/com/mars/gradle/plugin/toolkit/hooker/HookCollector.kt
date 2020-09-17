package com.mars.gradle.plugin.toolkit.hooker

import org.gradle.api.Project
import kotlin.reflect.KClass

/*
 * author: 凛
 * date: 2020/9/17 下午9:39
 * github: https://github.com/oh-Rin
 * description: 收集一切注册过的 Hooker
 */
internal val collected = mutableListOf<KClass<out TaskHooker>>()

/**
 * 将多个 Hooker 注册到 [collected]
 * NOTE: 注册后将会立刻进行挂钩
 */
fun Project.registerHooks(vararg hookers: KClass<out TaskHooker>) {
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