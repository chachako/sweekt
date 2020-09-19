package com.mars.preference

import com.mars.preference.core.SharedPrefModel
import com.mars.preference.kotpref.impl.enum.enumValue

enum class Enum {
  ONE, TWO, THREE, DEFAULT
}

data class JsonModel(val name: String)

object SharedExample : SharedPrefModel("example") {
  var str by string("default")
  var enum by enumValue(Enum.DEFAULT)
}

fun main() {
  // example: set, put
  SharedExample.str = "changed"
  println(SharedExample.str)
}