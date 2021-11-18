/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
package com.meowool.sweekt.info

import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.getAnnotationBooleanOrNull
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.resolve.annotations.argumentValue

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class Info(annotationData: Any) {
  val generateCopy = annotationData.getBooleanArgument("generateCopy") ?: true
  val generateEquals = annotationData.getBooleanArgument("generateEquals") ?: true
  val generateHashCode = annotationData.getBooleanArgument("generateHashCode") ?: true
  val generateToString = annotationData.getBooleanArgument("generateToString") ?: true
  val generateComponentN = annotationData.getBooleanArgument("generateComponentN") ?: true

  val joinPrivateProperties = annotationData.getBooleanArgument("joinPrivateProperties") ?: false
  val joinPrimaryProperties = annotationData.getBooleanArgument("joinPrimaryProperties") ?: true
  val joinBodyProperties = annotationData.getBooleanArgument("joinBodyProperties") ?: true

  val callSuperEquals = annotationData.getBooleanArgument("callSuperEquals") ?: true
  val callSuperHashCode = annotationData.getBooleanArgument("callSuperHashCode") ?: true
}

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class InfoInvisible(annotationData: Any) {
  val generateCopy = annotationData.getBooleanArgument("generateCopy") ?: false
  val generateEquals = annotationData.getBooleanArgument("generateEquals") ?: false
  val generateHashCode = annotationData.getBooleanArgument("generateHashCode") ?: false
  val generateToString = annotationData.getBooleanArgument("generateToString") ?: false
  val generateComponentN = annotationData.getBooleanArgument("generateComponentN") ?: false
}

/**
 * @author 凛 (https://github.com/RinOrz)
 */
private fun Any.getBooleanArgument(name: String): Boolean? = when (this) {
  is AnnotationDescriptor -> argumentValue(name)?.value?.castOrNull<Boolean>()
  is IrConstructorCall -> getAnnotationBooleanOrNull(name)
  else -> error("Unsupported annotation instance: $javaClass")
}
