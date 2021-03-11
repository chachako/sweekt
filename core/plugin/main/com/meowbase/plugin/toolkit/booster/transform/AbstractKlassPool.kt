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

package com.meowbase.plugin.toolkit.booster.transform

import java.io.File
import java.net.URLClassLoader

/**
 * Represents an abstraction of [KlassPool]
 *
 * @author johnsonlee
 */
abstract class AbstractKlassPool(
  private val classpath: Collection<File>,
  final override val parent: KlassPool? = null
) : KlassPool {

  private val classes = mutableMapOf<String, Klass>()

  protected val imports = mutableMapOf<String, Collection<String>>()

  override val classLoader: ClassLoader = URLClassLoader(classpath.map { it.toURI().toURL() }
    .toTypedArray(), parent?.classLoader)

  override operator fun get(type: String) = normalize(type).let { name ->
    classes.getOrDefault(name, findClass(name))
  }

  override fun close() {
    val classLoader = this.classLoader
    if (classLoader is URLClassLoader) {
      classLoader.close()
    }
  }

  override fun toString() = "classpath: $classpath"

  internal fun getImports(name: String): Collection<String> = this.imports[name]
    ?: this.parent?.let { it ->
      if (it is AbstractKlassPool) it.getImports(name) else null
    } ?: emptyList()

  internal fun findClass(name: String): Klass {
    return try {
      LoadedKlass(this, classLoader.loadClass(name)).also {
        classes[name] = it
      }
    } catch (e: Throwable) {
      DefaultKlass(name)
    }
  }

}

private class DefaultKlass(name: String) : Klass {

  override val qualifiedName: String = name

  override fun isAssignableFrom(type: String) = false

  override fun isAssignableFrom(klass: Klass) = klass.qualifiedName == this.qualifiedName

}

private class LoadedKlass(val pool: AbstractKlassPool, val clazz: Class<out Any>) : Klass {

  override val qualifiedName: String = clazz.name

  override fun isAssignableFrom(type: String) = isAssignableFrom(pool.findClass(normalize(type)))

  override fun isAssignableFrom(klass: Klass) = klass is LoadedKlass && clazz.isAssignableFrom(klass.clazz)

}

private fun normalize(type: String) = if (type.contains('/')) {
  type.replace('/', '.')
} else {
  type
}