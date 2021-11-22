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
@file:Suppress("SpellCheckingInspection", "CanBeParameter")

package com.meowool.sweekt

import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.copyAttributes
import org.jetbrains.kotlin.ir.util.DeepCopyIrTreeWithSymbols
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.SymbolRenamer
import org.jetbrains.kotlin.ir.util.TypeRemapper

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class FinalFieldTransformer(
  private val finalProperties: Set<IrProperty>,
  private val symbolRemapper: SymbolRemapper,
  private val typeRemapper: TypeRemapper,
  private val symbolRenamer: SymbolRenamer = SymbolRenamer.DEFAULT
) : DeepCopyIrTreeWithSymbols(symbolRemapper, typeRemapper) {

  override fun visitProperty(declaration: IrProperty): IrProperty = if (finalProperties.contains(declaration))
    declaration.factory.createProperty(
      declaration.startOffset, declaration.endOffset,
      mapDeclarationOrigin(declaration.origin),
      symbolRemapper.getDeclaredProperty(declaration.symbol),
      declaration.name,
      declaration.visibility,
      declaration.modality,
      isVar = true,
      isConst = declaration.isConst,
      isLateinit = declaration.isLateinit,
      isDelegated = declaration.isDelegated,
      isExternal = declaration.isExternal,
      isExpect = declaration.isExpect,
      containerSource = declaration.containerSource,
    ).apply {
      transformAnnotations(declaration)
      copyAttributes(declaration)
      this.backingField = declaration.backingField?.transform()?.also {
        it.correspondingPropertySymbol = symbol
      }
      this.getter = declaration.getter?.transform()?.also {
        it.correspondingPropertySymbol = symbol
      }
      this.setter = declaration.setter?.transform()?.also {
        it.correspondingPropertySymbol = symbol
      }
      this.overriddenSymbols = declaration.overriddenSymbols.map {
        symbolRemapper.getReferencedProperty(it)
      }
    } else super.visitProperty(declaration)

  override fun visitField(declaration: IrField): IrField = if (finalProperties.any { it.backingField == declaration })
    declaration.factory.createField(
      declaration.startOffset, declaration.endOffset,
      mapDeclarationOrigin(declaration.origin),
      symbolRemapper.getDeclaredField(declaration.symbol),
      symbolRenamer.getFieldName(declaration.symbol),
      declaration.type.remapType(),
      declaration.visibility,
      isFinal = false,
      isExternal = declaration.isExternal,
      isStatic = declaration.isStatic,
    ).apply {
      transformAnnotations(declaration)
      initializer = declaration.initializer?.transform()
    } else super.visitField(declaration)
}
