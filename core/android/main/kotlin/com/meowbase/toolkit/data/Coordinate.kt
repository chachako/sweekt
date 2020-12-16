package com.meowbase.toolkit.data

import com.meowbase.toolkit.float

data class Coordinate(val x: Int, val y: Int) {
  val xFloat = x.float
  val yFloat = y.float
}