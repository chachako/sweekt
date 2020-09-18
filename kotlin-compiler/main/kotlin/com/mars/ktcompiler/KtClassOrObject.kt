package com.mars.ktcompiler

import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile

/** 找出 Kt 文件源码中命中的 class 或 object */
inline fun KtFile.filterClassOrObject(
  byAnnotation: (KtAnnotationEntry) -> Boolean = { true },
): List<KtClassOrObject> = classesOrObjects.filter {
  var result = false
  // filter by annotation
  for (entry in it.annotationEntries) {
    if (byAnnotation(entry)) {
      result = true
      break
    }
  }
  result
}