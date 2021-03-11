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
 * The simple name of class
 */
val ClassNode.simpleName: String
  get() = this.name.substring(this.name.lastIndexOf('/') + 1)

/**
 * The name of class
 */
val ClassNode.className: String
  get() = name.replace('/', '.')

val ClassNode.isAnnotation: Boolean
  get() = 0 != (access and Opcodes.ACC_ANNOTATION)

val ClassNode.isInterface: Boolean
  get() = 0 != (access and Opcodes.ACC_INTERFACE)

val ClassNode.isAbstract: Boolean
  get() = 0 != (access and Opcodes.ACC_ABSTRACT)

val ClassNode.isPublic: Boolean
  get() = 0 != (access and Opcodes.ACC_PUBLIC)

val ClassNode.isProtected: Boolean
  get() = 0 != (access and Opcodes.ACC_PROTECTED)

val ClassNode.isPrivate: Boolean
  get() = 0 != (access and Opcodes.ACC_PRIVATE)

val ClassNode.isStatic: Boolean
  get() = 0 != (access and Opcodes.ACC_STATIC)

val ClassNode.isFinal: Boolean
  get() = 0 != (access and Opcodes.ACC_FINAL)

fun ClassNode.isInvisibleAnnotationPresent(annotations: Iterable<String>) = this.invisibleAnnotations?.map {
  it.desc
}?.any(annotations::contains) ?: false

fun ClassNode.isInvisibleAnnotationPresent(vararg annotations: String) = isInvisibleAnnotationPresent(
  annotations.asIterable()
)


fun ClassNode.isVisibleAnnotationPresent(annotations: Iterable<String>) = this.visibleAnnotations?.map {
  it.desc
}?.any(annotations::contains) ?: false

fun ClassNode.isVisibleAnnotationPresent(vararg annotations: String) = isVisibleAnnotationPresent(
  annotations.asIterable()
)

val ClassNode.defaultClinit: MethodNode
  get() = MethodNode(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null).apply {
    maxStack = 1
    instructions.add(InsnNode(Opcodes.RETURN))
  }

val ClassNode.defaultInit: MethodNode
  get() = MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null).apply {
    maxStack = 1
    instructions.add(InsnList().apply {
      add(VarInsnNode(Opcodes.ALOAD, 0))
      add(MethodInsnNode(Opcodes.INVOKESPECIAL, superName, name, desc, false))
      add(InsnNode(Opcodes.RETURN))
    })
  }

val ClassNode.defaultOnCreate: MethodNode
  get() = MethodNode(Opcodes.ACC_PUBLIC, "onCreate", "()V", null, null).apply {
    instructions.add(InsnList().apply {
      add(VarInsnNode(Opcodes.ALOAD, 0))
      add(MethodInsnNode(Opcodes.INVOKESPECIAL, superName, name, desc, false))
      add(InsnNode(Opcodes.RETURN))
    })
    maxStack = 1
  }
