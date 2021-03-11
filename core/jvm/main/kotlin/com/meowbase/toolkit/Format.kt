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

package com.meowbase.toolkit

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

/**
 * 将 [Long] 值格式化为人类可读的字符串
 * @param digits 决定返回时要显示多少位数，例如: 3 -> 100.0; 4 -> 100.00; 5 -> 100.000
 */
fun Long.formatSize(digits: Int = 3): String {
  val size = this
  require(digits >= 0) { "Precision must be greater than or equal to zero. Was: $digits" }
  require(size >= 0) { "Size to format must be greater than or equal to zero. Was: $size" }

  if (size < 1024) {
    return "$size B"
  }

  // A modified strategy based off the idea of counting leading zeros from: https://stackoverflow.com/a/24805871
  val magnitude = (63 - java.lang.Long.numberOfLeadingZeros(size)) / 10
  @Suppress("SpellCheckingInspection") val unitPrefix = "KMGTPE"[magnitude - 1]

  // Instead of directly converting to double, truncate to an intermediate so we're working with values < 1024^2
  val truncatedSize = size / (1L shl ((magnitude - 1) * 10))
  val finalDouble = truncatedSize.toDouble() / 1024

  // If it's above our precision value then don't display any decimal places
  if (finalDouble >= 10.0.pow(digits - 1)) {
    return "${finalDouble.toInt()} ${unitPrefix}B"
  }

  // Otherwise display at least 1
  val mathContext = MathContext(digits, RoundingMode.DOWN)
  val bigDecimal = BigDecimal(finalDouble, mathContext)
  val formatter = DecimalFormat("#.0" + "#".repeat(0.coerceAtLeast(digits - 2)))
  return "${formatter.format(bigDecimal)} ${unitPrefix}B"
}