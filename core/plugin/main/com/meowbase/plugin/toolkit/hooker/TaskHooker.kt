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

@file:[Suppress("MemberVisibilityCanBePrivate") OptIn(ExperimentalTime::class)]

package com.meowbase.plugin.toolkit.hooker

import com.meowbase.plugin.toolkit.data.AndroidTasks
import com.meowbase.plugin.toolkit.ktx.findAndroidTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/*
 * author: 凛
 * date: 2020/9/17 下午8:36
 * github: https://github.com/RinOrz
 * description: 拦截任务执行时的开始或结尾
 */
abstract class TaskHooker(
  /** @see Project.findAndroidTask */
  private val hookName: String
) {
  constructor(hook: AndroidTasks): this(hook.taskName)

  lateinit var project: Project
  private val logger: Logger get() = project.logger
  private val taskOrNull: Task? get() = project.findAndroidTask(hookName)
  private val hookerName by lazy { javaClass.simpleName }

  /** 等价于 [Task.doFirst] */
  open fun onBefore(): TaskAction? = null

  /** 等价于 [Task.doFirst] */
  open fun onAfter(): TaskAction? = null

  /**
   *
   * │ sth...
   *
   */
  fun logTrace(trace: String, throwable: Throwable? = null) = when(throwable) {
    null -> logger.info("$HORIZONTAL_LINE $trace")
    else -> logger.info("$HORIZONTAL_LINE $trace", throwable)
  }
  fun logDebug(debug: String, throwable: Throwable? = null) = when(throwable) {
    null -> logger.info("$HORIZONTAL_LINE $debug")
    else -> logger.info("$HORIZONTAL_LINE $debug", throwable)
  }
  fun logInfo(info: String, throwable: Throwable? = null) = when(throwable) {
    null -> logger.info("$HORIZONTAL_LINE $info")
    else -> logger.info("$HORIZONTAL_LINE $info", throwable)
  }
  fun logWarn(warn: String, throwable: Throwable? = null) = when(throwable) {
    null -> logger.warn("$HORIZONTAL_LINE $warn")
    else -> logger.warn("$HORIZONTAL_LINE $warn", throwable)
  }
  fun logError(error: String, throwable: Throwable? = null) = when(throwable) {
    null -> logger.error("$HORIZONTAL_LINE $error")
    else -> logger.error("$HORIZONTAL_LINE $error", throwable)
  }

  /**
   * 运行并输出信息
   *
   * ```
   *  ┌──────────────────────────────────────────────
   *  │ [xHooker] Hooking task-start:
   *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
   *  │ ...
   *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
   *  │ Hook finished, time-consuming 5s.
   *  └──────────────────────────────────────────────
   * ```
   */
  private fun action(block: () -> Unit) {
    logger.info(TOP_BORDER)
    logInfo("[$hookerName] Hooking task-start:")
    logger.info(MIDDLE_BORDER)
    val consuming = measureTime(block)
    logger.info(MIDDLE_BORDER)
    logInfo("Hook finished, time-consuming ${consuming.inSeconds}s.")
    logger.info(BOTTOM_BORDER)
  }

  internal fun hook() {
    taskOrNull?.apply {
      val hookBefore = onBefore()
      val hookAfter = onAfter()
      if (hookBefore != null) doFirst { action { hookBefore() } }
      if (hookAfter != null) doLast { action { hookAfter() } }
    }
  }

  companion object {
    /**
     * Drawing toolbox
     */
    private const val TOP_LEFT_CORNER = '┌'
    private const val BOTTOM_LEFT_CORNER = '└'
    private const val MIDDLE_CORNER = '├'
    private const val HORIZONTAL_LINE = '│'
    private const val DOUBLE_DIVIDER = "────────────────────────────────────────────────────────"
    private const val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    private const val TOP_BORDER = TOP_LEFT_CORNER.toString() + DOUBLE_DIVIDER
    private const val BOTTOM_BORDER = BOTTOM_LEFT_CORNER.toString() + DOUBLE_DIVIDER
    private const val MIDDLE_BORDER = MIDDLE_CORNER.toString() + SINGLE_DIVIDER
  }
}

typealias TaskAction = Task.() -> Unit