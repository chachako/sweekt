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

import com.meowbase.toolkit.existOrMkdirs
import com.meowbase.toolkit.existOrNewFile
import com.meowbase.toolkit.children
import com.meowbase.toolkit.okio.appendText
import com.meowbase.toolkit.okio.copyTo
import com.meowbase.toolkit.okio.readText
import com.meowbase.toolkit.okio.writeText
import com.meowbase.toolkit.subtree
import com.meowbase.toolkit.tree
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.io.File
import kotlin.io.copyRecursively

/*
 * author: 凛
 * date: 2020/9/7 上午1:21
 * github: https://github.com/RinOrz
 * description: FileToolkit 测试案例
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FileTest {
  private val workDir = File(javaClass.classLoader.getResource("file")!!.path)
  private val textTxt = workDir.resolve("text.txt")
  private val copiedDir = workDir.resolve("copied")

  @Test fun `A-Test clear cache`() {
    copiedDir.deleteRecursively()
    assertTrue(!copiedDir.exists())
  }

  @Test fun `B-Test prepare files`() {
    println("Work directory = ${workDir.absolutePath}\n")
    copiedDir.existOrMkdirs()
    assertTrue(copiedDir.exists())
  }

  @Test fun `C-Test file copy and overwrite`() {
    val copiedTextTxt = copiedDir.resolve("text.txt")
    textTxt.copyTo(copiedTextTxt)
    assertTrue(copiedTextTxt.exists())
    // 创建空白文件
    val overwrite = copiedDir.resolve("overwrite.txt").existOrNewFile()
    // 复制并覆盖
    textTxt.copyTo(overwrite, overwrite = true)
  }

  @Test fun `D-Test file content`() {
    val text = copiedDir.resolve("text.txt").readText()
    val overwrite = copiedDir.resolve("overwrite.txt")
    assertTrue(text == overwrite.readText())
    assertEquals(text, "正常的文本数据")

    // 测试追加文本
    overwrite.appendText("\n新增的行数")
    assertEquals(overwrite.readText(), """
      正常的文本数据
      新增的行数
    """.trimIndent())

    // 测试覆盖文本
    overwrite.writeText("内容被覆盖")
    assertEquals(overwrite.readText(), "内容被覆盖")
  }

  @Test fun `E-Test file tree`() {
    fun Sequence<File>.mapAll() = map { it.name }.toList()

    val multilayerDir = workDir.resolve("multilayer")
    println("""
      File tree = {
        ${multilayerDir.tree().mapAll().joinToString()}
      }
      
    """.trimIndent())
    // 测试完整目录树是否正确
    assertTrue(
      multilayerDir.tree().mapAll().containsAll(
        listOf(
          "multilayer",
          "1", "smallest", "smallest_1.txt", "1_1.txt", "1_2.txt",
          "2", "smallest", "smallest_2.txt", "2_1.txt", "2_2.txt",
          "1", "smallest", "smallest_3.txt", "3_1.txt", "3_2.txt",
          "deep", "1dir", "2dir", "3dir", "End",
        )
      )
    )

    // 测试子目录树是否正确
    assertTrue(
      multilayerDir.subtree().mapAll().containsAll(
        listOf(
          "1", "smallest", "smallest_1.txt", "1_1.txt", "1_2.txt",
          "2", "smallest", "smallest_2.txt", "2_1.txt", "2_2.txt",
          "1", "smallest", "smallest_3.txt", "3_1.txt", "3_2.txt",
          "deep", "1dir", "2dir", "3dir", "End",
        )
      )
    )

    // 只测试目录的所有直接子文件/目录
    assertTrue(
      multilayerDir.children().mapAll().containsAll(
        listOf(
          "1",
          "2",
          "1",
          "deep",
        )
      )
    )

    // 测试 2 层目录树是否正确
    assertTrue(
      multilayerDir.subtree(2).mapAll().containsAll(
        listOf(
          "1", "1_1.txt", "1_2.txt",
          "2", "2_1.txt", "2_2.txt",
          "1", "3_1.txt", "3_2.txt",
          "deep", "1dir",
        )
      )
    )

    // 测试 3 层目录树是否正确
    assertTrue(
      multilayerDir.subtree(3).mapAll().containsAll(
        listOf(
          "1", "smallest", "smallest_1.txt", "1_1.txt", "1_2.txt",
          "2", "smallest", "smallest_2.txt", "2_1.txt", "2_2.txt",
          "1", "smallest", "smallest_3.txt", "3_1.txt", "3_2.txt",
          "deep", "1dir", "2dir",
        )
      )
    )
  }


  @Test fun `F-Test copy file tree`() {
    fun Sequence<File>.mapAll() = map { it.name }.toList()

    val multilayerDir = workDir.resolve("multilayer")
    val resultDir = workDir.resolve("multilayer-copied")

    multilayerDir.copyRecursively(resultDir)
    assertEquals(multilayerDir.subtree().mapAll(), resultDir.subtree().mapAll())

    // 测试 multilayer-copied/1/1_1.txt 文件修改
    val file = resultDir.resolve("1/1_1.txt")

    val newContent = System.currentTimeMillis().toString()
    file.writeText(newContent)
    assertEquals(file.readText(), newContent)

    // 判断刚刚被修改的文本是否成功被原文件覆盖
    multilayerDir.copyRecursively(resultDir, overwrite = true)
    assertTrue(file.readText().isEmpty())
  }
}