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

@file:Suppress("PackageDirectoryMismatch", "UnstableApiUsage", "DEPRECATION")

// TODO com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

/**
 * 能让 kotlin 脚本拥有与 groovy 脚本的 sourceSets 相同的行为
 * ```
 * sourceSets {
 *   main {
 *     java.srcFiles("main/kotlin")
 *     manifest.srcFiles("main/AndroidManifest.xml")
 *   }
 *   test {
 *     java.srcFiles("test/kotlin")
 *   }
 * }
 * ```
 */

val SourceSetContainer.main: SourceSet get() = getByName("main")
val SourceSetContainer.test: SourceSet get() = getByName("test")
fun SourceSetContainer.main(block: SourceSet.() -> Unit) = main.apply(block)
fun SourceSetContainer.test(block: SourceSet.() -> Unit) = test.apply(block)

val NamedDomainObjectContainer<out AndroidSourceSet>.main: AndroidSourceSet get() = getByName("main")
val NamedDomainObjectContainer<out AndroidSourceSet>.test: AndroidSourceSet get() = getByName("test")
val NamedDomainObjectContainer<out AndroidSourceSet>.androidTest: AndroidSourceSet
  get() = getByName("androidTest")

fun NamedDomainObjectContainer<out AndroidSourceSet>.main(
  block: AndroidSourceSet.() -> Unit
) = main.apply(block)

fun NamedDomainObjectContainer<out AndroidSourceSet>.test(
  block: AndroidSourceSet.() -> Unit
) = test.apply(block)

fun NamedDomainObjectContainer<out AndroidSourceSet>.androidTest(
  block: AndroidSourceSet.() -> Unit
) = androidTest.apply(block)