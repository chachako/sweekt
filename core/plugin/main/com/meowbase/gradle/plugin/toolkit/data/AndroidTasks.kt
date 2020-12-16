package com.meowbase.gradle.plugin.toolkit.data

/*
 * author: 凛
 * date: 2020/9/18 上午1:14
 * github: https://github.com/RinOrz
 * description: 一些内置的 Android 项目 tasks 名称
 */
enum class AndroidTasks(val taskName: String) {
  /** Android 项目编译时 */
  Assemble("assemble**"),
  /** Android 项目打包时 */
  Packaging("package**"),
  /** Dex 合并时 */
  DexMerge("mergeDex**"),
  /** 原生库合并时 */
  NativeLibsMerge("merge**NativeLibs")
}