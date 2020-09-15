@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.mars.toolkit.data

import com.mars.toolkit.StringJudge
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