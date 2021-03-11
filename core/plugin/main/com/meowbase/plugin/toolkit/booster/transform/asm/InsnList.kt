/*
 * Copyright (c) 2021. Rin Orz (凛)
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
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.plugin.toolkit.booster.transform.asm

import com.meowbase.toolkit.iterations.asIterable
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

fun InsnList.asIterable() = this.iterator().asIterable()

/**
 * Find the first instruction node with the specified opcode
 *
 * @param opcode The opcode to search
 */
fun InsnList.find(opcode: Int) = this.asIterable().find { it.opcode == opcode }

/**
 * Find all of instruction nodes with the specified opcode
 *
 * @param opcodes The opcode to search
 */
fun InsnList.findAll(vararg opcodes: Int) = this.filter { it.opcode in opcodes }

fun InsnList.filter(predicate: (AbstractInsnNode) -> Boolean) = this.asIterable().filter(predicate)

fun InsnList.any(predicate: (AbstractInsnNode) -> Boolean) = this.asIterable().any(predicate)
