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
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowool.sweekt

import org.jetbrains.kotlin.name.FqName

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object SweektNames {
  const val Root = "com.meowool.sweekt"

  val JvmField = FqName(JvmField::class.java.name)
  val ContentEquals = FqName("kotlin.collections.contentEquals")

  val LazyInit = fqNameFor("LazyInit")
  val resetLazyValue = fqNameFor("resetLazyValue")
  val resetLazyValues = fqNameFor("resetLazyValues")

  val Info = fqNameFor("Info")
  val InfoInvisible = fqNameFor("Info.Invisible")
  val InfoSynthetic = FqName("Info.Synthetic")
  val FullInfoSynthetic = FqName("$Root.$InfoSynthetic")

  fun root() = FqName(Root)
  fun fqNameFor(cname: String) = FqName("$Root.$cname")
}
