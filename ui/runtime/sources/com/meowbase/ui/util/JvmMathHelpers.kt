package com.meowbase.ui.util

fun Float.toStringAsFixed(digits: Int): String = String.format("%.${digits}f", this)