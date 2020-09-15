package com.mars.toolkit.zip

import java.util.zip.ZipEntry

/*
 * author: 凛
 * date: 2020/9/10 下午9:59
 * github: https://github.com/oh-Rin
 * description: 定义压缩模式
 */
data class CompressMode(val native: Int) {
  companion object {
    /** 通常的压缩方式 */
    val Deflated = CompressMode(ZipEntry.DEFLATED)
    /** 仅打包归档储存，不进行压缩 */
    val Stored = CompressMode(ZipEntry.STORED)
  }
}