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

@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.meowbase.toolkit.data

import com.meowbase.toolkit.StringJudge
import okio.Source
import java.io.File
import java.util.zip.Deflater

/**
 * 声明一个 Zip 文件
 * @property file Zip 文件
 * @property level Zip 压缩等级 [Deflater]
 * @property bufferSize 解压缩时的 IO 缓冲区大小
 */
data class Zip(val file: File, var level: Int, var bufferSize: Int?)

/**
 * 定义一个 Zip 条目
 *
 * @property name 条目名称
 * @property data 条目内容数据
 * @property overwrite 是否覆盖 Zip 文件中已经存在的同名条目
 */
data class ZipEntry(val name: String, val data: Source, var overwrite: Boolean = false)

/**
 * Zip 条目的描述
 * 用于某些特殊情况下的条目区分
 *
 * @param name zip 条目名称
 * @param pattern 根据模式来决定使用哪个方法来判断 [name]
 */
data class ZipEntryDescribe(val name: String, val pattern: StringJudge)