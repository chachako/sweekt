package com.mars.ktcompiler.psi

import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry

val KtClassOrObject.superTypeCall: KtSuperTypeCallEntry?
  get() = getSuperTypeList()?.firstChild as? KtSuperTypeCallEntry

/** 找出 Kt 文件源码中命中的 class 或 object */
fun KtFile.filterClassOrObject(
  byAnnotation: ((KtAnnotationEntry) -> Boolean)? = null,
): List<KtClassOrObject> = classesOrObjects.filter {
  var result = true

  // filter by annotation
  byAnnotation?.also { request ->
    if (it.annotationEntries.none(request))
      result = false
  }

  result
}