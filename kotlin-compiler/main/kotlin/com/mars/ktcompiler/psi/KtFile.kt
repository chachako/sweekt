package com.mars.ktcompiler.psi

import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile

/** 返回源文件中的所有 class 或 object */
val KtFile.classesOrObjects: List<KtClassOrObject>
  get() = children.filterIsInstance<KtClassOrObject>()