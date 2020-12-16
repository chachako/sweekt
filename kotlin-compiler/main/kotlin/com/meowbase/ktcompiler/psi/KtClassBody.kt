package com.meowbase.ktcompiler.psi

import org.jetbrains.kotlin.psi.*

fun KtClassBody.filterProperties(
  byAnnotation: ((KtAnnotationEntry) -> Boolean)? = null,
  byDelegate: ((KtPropertyDelegate) -> Boolean)? = null,
  byDelegateExpression: ((KtExpression) -> Boolean)? = null,
): List<KtProperty> = properties.filter {
  var result = true

  // filter by annotation
  byAnnotation?.also { request ->
    if (it.annotationEntries.none(request))
      result = false
  }

  // filter by delegate
  byDelegate?.also { request ->
    if (it.delegate == null || !request(it.delegate!!)) {
      result = false
    }
  }

  // filter by delegate expression
  byDelegateExpression?.also { request ->
    if (it.delegateExpression == null || !request(it.delegateExpression!!)) {
      result = false
    }
  }

  result
}