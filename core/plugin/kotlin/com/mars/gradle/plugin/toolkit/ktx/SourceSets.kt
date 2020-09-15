@file:Suppress("PackageDirectoryMismatch", "UnstableApiUsage")

import com.android.build.api.dsl.AndroidSourceSet
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
  get() = getByName(
    "androidTest"
  )

fun NamedDomainObjectContainer<out AndroidSourceSet>.main(
  block: AndroidSourceSet.() -> Unit
) = main.apply(block)

fun NamedDomainObjectContainer<out AndroidSourceSet>.test(
  block: AndroidSourceSet.() -> Unit
) = test.apply(block)

fun NamedDomainObjectContainer<out AndroidSourceSet>.androidTest(
  block: AndroidSourceSet.() -> Unit
) = androidTest.apply(block)