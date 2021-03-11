/**
 * 加载 Kotlin 代码以解决 Toolkit 插件的 Class '**' is compiled by a new Kotlin compiler backend and cannot be loaded by the old compiler
 * FIXME 等什么时候 Gradle 内置了 1.4 版本的 Kotlin 后才能够删除这些代码（该死的 Gradle）
 * https://github.com/gradle/gradle/issues/15335
 */
plugins { `kotlin-dsl` }; fixOldCompilerWarn()