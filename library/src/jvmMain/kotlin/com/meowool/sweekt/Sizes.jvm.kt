@file:Suppress("SpellCheckingInspection", "NAME_SHADOWING")

package com.meowool.sweekt

import kotlin.math.abs

actual object DefaultDataSizeUnits : DataSizeUnits {
  override var useSI: Boolean = true
  override val kibi: String
    get() = if (useSI) "K" else "Ki"
  override val mebi: String
    get() = if (useSI) "M" else "Mi"
  override val gibi: String
    get() = if (useSI) "G" else "Gi"
  override val tebi: String
    get() = if (useSI) "T" else "Ti"
  override val pebi: String
    get() = if (useSI) "P" else "Pi"
  override val exbi: String
    get() = if (useSI) "E" else "Ei"
}

/**
 * Converts the long size to human-readable string.
 *
 * For precision 1 example:
 * ```
 *                              SI     BINARY
 *
 *                   0:        0 B        0 B
 *                  27:       27 B       27 B
 *                 999:      999 B      999 B
 *                1000:     1.0 kB     1000 B
 *                1023:     1.0 kB     1023 B
 *                1024:     1.0 kB    1.0 KiB
 *                1728:     1.7 kB    1.7 KiB
 *              110592:   110.6 kB  108.0 KiB
 *             7077888:     7.1 MB    6.8 MiB
 *           452984832:   453.0 MB  432.0 MiB
 *         28991029248:    29.0 GB   27.0 GiB
 *       1855425871872:     1.9 TB    1.7 TiB
 * 9223372036854775807:     9.2 EB    8.0 EiB   (Long.MAX_VALUE)
 * ```
 *
 * [Initial source](https://stackoverflow.com/a/3758880)
 * [For more details](https://en.wikipedia.org/wiki/Binary_prefix)
 *
 * @param precision the precision of result size.
 * @param prefix the string added before to the result size string.
 * @param suffix the string added after to the result size string.
 * @param separator the string added after size and before unit.
 * @param units determine the name of unit used to result size string.
 * @param useSI to see [DataSizeUnits.useSI].
 *
 * @see DataSizeUnits
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun Long.toReadableSize(
  precision: Int,
  prefix: String?,
  suffix: String?,
  separator: String?,
  units: DataSizeUnits,
  useSI: Boolean,
): String {
  units.useSI = useSI

  val units = arrayOf(
    units.kibi,
    units.mebi,
    units.gibi,
    units.tebi,
    units.pebi,
    units.exbi
  )

  val result = if (useSI) {
    this.toHumanReadableSI(units, separator, precision)
  } else {
    this.toHumanReadableBin(units, separator, precision)
  }

  return buildString {
    prefix?.apply(::append)
    append(result)
    suffix?.apply(::append)
  }
}

/**
 * Converts the long size to human-readable string.
 *
 * @param precision the precision of result size.
 * @param prefix the string added before to the result size string.
 * @param suffix the string added after to the result size string.
 * @param separator the string added after size and before unit.
 * @param units determine the name of unit used to result size string.
 * @param useSI to see [DataSizeUnits.useSI].
 *
 * @see DataSizeUnits
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun Number.toReadableSize(
  precision: Int,
  prefix: String?,
  suffix: String?,
  separator: String?,
  units: DataSizeUnits,
  useSI: Boolean,
): String = asLong().toReadableSize(precision, prefix, suffix, separator, units, useSI)

private fun Long.toHumanReadableSI(units: Array<String>, separator: String?, precision: Int): String {
  var bytes = this
  if (-1000 < bytes && bytes < 1000) {
    return buildString {
      append(bytes)
      separator?.apply(::append)
    }
  }

  var index = 0
  while (bytes <= -999950 || bytes >= 999950) {
    bytes /= 1000
    index++
  }

  return buildString {
    append("%.${precision}f".format(bytes / 1000.0))
    separator?.apply(::append)
    append(units[index])
  }
}

private fun Long.toHumanReadableBin(units: Array<String>, separator: String?, precision: Int): String {
  val bytes = this

  val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
  if (absB < 1024) {
    return buildString {
      append(bytes)
      separator?.apply(::append)
    }
  }
  var value = absB
  var i = 40

  var index = 0
  while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
    value = value shr 10
    i -= 10
    index++
  }
  value *= bytes shr 63 or (-bytes ushr 63)

  return buildString {
    append("%.${precision}f".format(value / 1024.0))
    separator?.apply(::append)
    append(units[index])
  }
}