# Toolkit ![Bintray](https://img.shields.io/bintray/v/oh-rin/Mars/gradle-toolkit?color=B87CEF)

一套由各式各样且功能强大的小工具（Gradle-Plugin、UI-Kit、Ktx 等...）组成的工具包，皆在帮助 Kotlin 开发者快速完成开发，减少代码繁琐性。

# TODO 文档建设中

**已开源依赖**：

[Core-Plugin](https://github.com/MarsPlanning/toolkit/tree/master/core/plugin) - 核心 Gradle 插件，可简化大量的 gradle.kts 工作：[示例](https://github.com/MarsPlanning/toolkit/blob/master/settings.gradle.kts#L36)
```kotlin
com.mars.gradle.plugin:toolkit:$version
```

[Core-Jvm](https://github.com/MarsPlanning/toolkit/tree/master/core/jvm) - 适用于 JVM 平台的核心工具：[示例](https://github.com/MarsPlanning/toolkit/tree/master/core/jvm/test)
```kotlin
com.mars.library:toolkit-core-jvm:$version
```

[Core-Android](https://github.com/MarsPlanning/toolkit/tree/master/core/android) - 适用于 Android 平台的核心工具
```kotlin
com.mars.library:toolkit-core-android:$version
```

[UI-Kit](https://github.com/MarsPlanning/toolkit/tree/master/core/plugin) - 以 AndroidViews 衍生的一个强大的 UI 工具包：[示例](https://github.com/MarsPlanning/toolkit/tree/master/ui/samples)
```kotlin
// UiKit 的核心 Gradle 插件（必要）
com.mars.library:toolkit-ui-plugin:$version

// UiKit 的核心运行时代码（必要）
com.mars.library:toolkit-ui-runtime:$version

// 提供 [Coil](https://github.com/coil-kt) 在 UiKit 中的增强
com.mars.library:toolkit-ui-extension-coil:$version
```