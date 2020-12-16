package com.meowbase.ktcompiler.psi

import org.jetbrains.kotlin.psi.ValueArgument

fun List<ValueArgument>.hasArgumentName(argName: String): Boolean =
  firstOrNull { it.getArgumentName()?.asName?.toString() == argName } != null