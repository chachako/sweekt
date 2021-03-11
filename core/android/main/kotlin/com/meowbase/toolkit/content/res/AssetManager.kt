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

package com.meowbase.toolkit.content.res

import android.content.Context
import android.content.res.AssetManager
import com.meowbase.toolkit.appContext
import com.meowbase.toolkit.existOrMkdir
import com.meowbase.toolkit.okio.writeSource
import okio.source
import java.io.File


/**
 * 从给定的上下文的 assets 中复制文件
 *
 * @param assetsPath assets 中的源文件路径
 * @param target 复制到的 [File] 实例
 */
fun Context.copyAssets(assetsPath: String, target: File): Boolean {
  var result = true
  val assetManger = assets as AssetManager
  try {
    // 判断 assets 目录下有没有子文件
    assetManger.list(assetsPath).takeUnless { it.isNullOrEmpty() }?.forEach { subPath: String ->
      // 遍历每一个文件夹
      result = result and copyAssets(
        assetsPath = "$assetsPath/$subPath",
        target = target.resolve(subPath)
      )
    } ?: target.apply {
      // 从 assets 中复制单个文件
      parentFile?.existOrMkdir(force = true)
      writeSource(assetManger.open(assetsPath).source())
    }
  } catch (e: Throwable) {
    e.printStackTrace()
    result = false
  }
  return result
}

/**
 * 从 app 的全局 assets 中复制文件
 *
 * @param assetsPath assets 中的源文件路径
 * @param target 目标复制的 [File] 实例
 */
fun copyAssets(assetsPath: String, target: File): Boolean =
  appContext.copyAssets(assetsPath, target)

/**
 * 从给定的上下文的 assets 中复制文件
 *
 * @param assetsPath assets 中的源文件路径
 * @param targetPath 复制到的文件 / 文件夹路径
 */
fun Context.copyAssets(assetsPath: String, targetPath: String): Boolean =
  copyAssets(assetsPath, File(targetPath))

/**
 * 从 app 的全局 assets 中复制文件
 *
 * @param assetsPath assets 中的源文件路径
 * @param targetPath 复制到的文件 / 文件夹路径
 */
fun copyAssets(assetsPath: String, targetPath: String): Boolean =
  appContext.copyAssets(assetsPath, targetPath)
