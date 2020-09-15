package com.mars.toolkit.data

import com.mars.toolkit.float

data class Coordinate(val x: Int, val y: Int) {
  val xFloat = x.float
  val yFloat = y.float
}