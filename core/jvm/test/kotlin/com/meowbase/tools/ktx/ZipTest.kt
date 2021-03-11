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

package com.meowbase.tools.ktx

import com.meowbase.toolkit.children
import com.meowbase.toolkit.okio.readText
import com.meowbase.toolkit.subtree
import com.meowbase.toolkit.zip.flatWriteTo
import com.meowbase.toolkit.zip.openZip
import com.meowbase.toolkit.zip.toZip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.io.File

/*
 * author: 凛
 * date: 2020/9/10 下午5:40
 * github: https://github.com/RinOrz
 * description: ZipToolkit 测试案例
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ZipTest {
  private val folder = File(javaClass.classLoader.getResource("file")!!.path)
  private val workDir = File(javaClass.classLoader.getResource("zip")!!.path)
  private val decompressDir = workDir.resolve("unzip")

  @Test fun `A-Test clear cache`() {
    workDir.subtree().forEach { it.delete() }
    decompressDir.subtree().forEach { it.delete() }
    println("Work directory = ${workDir.absolutePath}\n")
  }

  @Test fun `B-Test build sub files to zip`() {
    val target = workDir.resolve("sub-archive.zip")
    // 打包目录下的所有直接文件 & 目录
    val zip = folder.children().toZip()

    // 生成到 sub-archive.zip 中
    zip.buildTo(target, overwrite = true)

    // 打开生成后的 zip 文件并对比压缩包的条目名称
    assertEquals(zip.map { it.name }, target.openZip().map { it.name })
  }

  @Test fun `C-Test flat build sub files to zip`() {
    val target = workDir.resolve("sub-archive-flat.zip")
    // 打包目录下的所有直接文件 & 目录，并平摊到压缩包根目录
    val zip = folder.children()
      .toSet() // 过滤重复名称的 File，否则添加重复 ZipEntry 会出错
      .toZip()
    zip.flatBuildTo(target, overwrite = true)
  }

  @Test fun `D-Test zip content`() {
    val target = workDir.resolve("sub-archive-flat.zip")
    val zipEntry = target.openZip().first { it.name == "text.txt" }
    assertEquals(zipEntry.data.readText(), "正常的文本数据")
  }

  @Test fun `E-Test decompress`() {
    val target = workDir.resolve("sub-archive.zip")
    target.openZip().flatWriteTo(decompressDir)
    assertTrue(
      decompressDir.subtree().map { it.name }.toList().containsAll(
        listOf(
          "1_1.txt",
          "1_2.txt",
          "2_1.txt",
          "2_2.txt",
          "3_1.txt",
          "3_2.txt",
          "smallest_1.txt",
          "smallest_2.txt",
          "smallest_3.txt",
          "End",
          "text.txt",
        )
      )
    )
  }
}