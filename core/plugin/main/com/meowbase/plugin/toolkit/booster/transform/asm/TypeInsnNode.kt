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

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

/**
 * Replace `new Type()` with `ShadowType.newType()`
 *
 * @author johnsonlee
 */
fun TypeInsnNode.transform(
  klass: ClassNode,
  method: MethodNode,
  instantializer: TypeInsnNode,
  type: String,
  prefix: String = ""
) {
  var next: AbstractInsnNode? = this.next

  loop@ while (null != next) {
    if (Opcodes.INVOKESPECIAL != next.opcode) {
      next = next.next
      continue
    }

    val invoke = next as MethodInsnNode
    if (this.desc == invoke.owner && "<init>" == invoke.name) {
      // replace NEW with INVOKESTATIC
      invoke.owner = type
      invoke.name = "new$prefix${instantializer.desc.substring(instantializer.desc.lastIndexOf('/') + 1)}"
      invoke.desc = "${
        invoke.desc.substring(
          0,
          invoke.desc.lastIndexOf(')')
        )
      })L${instantializer.desc};"
      invoke.opcode = Opcodes.INVOKESTATIC
      invoke.itf = false

      // remove the next DUP of NEW
      val dup = instantializer.next
      if (Opcodes.DUP == dup.opcode) {
        method.instructions.remove(dup)
      } else {
        TODO("Unexpected instruction ${dup.opcode}: ${klass.name}.${method.name}${method.desc}")
      }
      method.instructions.remove(instantializer)
      break@loop
    }

    next = next.next
  }
}
