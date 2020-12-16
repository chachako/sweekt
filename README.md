# Toolkit ![Bintray](https://img.shields.io/bintray/v/oh-rin/Meowbase/gradle-toolkit?color=B87CEF)

一套由各式各样且功能强大的小工具（Gradle-Plugin、UI-Kit、Ktx 等...）组成的工具包，皆在帮助 Kotlin 开发者快速完成开发，减少代码繁琐性。

# TODO 建设文档与每个小工具的对应示例项目

**已开源依赖**：

[Core-Plugin](https://github.com/MeowBase/toolkit/tree/master/core/plugin) - 核心 Gradle 插件，可简化大量的 gradle.kts 工作：[示例](https://github.com/MeowBase/toolkit/blob/master/settings.gradle.kts#L36)
```kotlin
com.meowbase.gradle.plugin:toolkit:$version
```

[Core-Jvm](https://github.com/MeowBase/toolkit/tree/master/core/jvm) - 适用于 JVM 平台的核心工具：[示例](https://github.com/MeowBase/toolkit/tree/master/core/jvm/test)
```kotlin
com.meowbase.library:toolkit-core-jvm:$version
```

[Core-Android](https://github.com/MeowBase/toolkit/tree/master/core/android) - 适用于 Android 平台的核心工具
```kotlin
com.meowbase.library:toolkit-core-android:$version
```

[Preference](https://github.com/MeowBase/toolkit/tree/master/preference) - 适用于 Android 平台的 Key-Value 储存工具
```kotlin
// Preference 核心运行时代码 (默认为 SharedPreference 模型)，实现灵感来源于 [KotPref](https://github.com/chibatching/Kotpref)（必须）
com.meowbase.library:toolkit-preference-plugin:$version

// 对 [MMKV](https://github.com/Tencent/MMKV) 提供 Preference 模型的扩展支持 -> [MmkvModel](https://github.com/MeowBase/toolkit/blob/master/preference/runtime/mmkv/native/sources/com/meowbase/preference/mmkv/MmkvModel.kt)
com.meowbase.library:toolkit-preference-plugin:$version

// 为所有 Preference 模型提供混淆支持（可选）
com.meowbase.library:toolkit-preference-plugin:$version
```

[UI-Kit](https://github.com/MeowBase/toolkit/tree/master/ui) - 以 AndroidViews 衍生的一个强大的 UI 工具包：[示例](https://github.com/MeowBase/toolkit/tree/master/ui/samples)
```kotlin
// UiKit 的核心 Gradle 插件（必须）
com.meowbase.library:toolkit-ui-plugin:$version

// UiKit 的核心运行时代码（必须）
com.meowbase.library:toolkit-ui-runtime:$version

// 提供 [Coil](https://github.com/coil-kt) 在 UiKit 中的增强
com.meowbase.library:toolkit-ui-extension-coil:$version

// 提供 [ConsecutiveScroller](https://github.com/donkingliang/ConsecutiveScroller) 在 UiKit 中的增强
com.meowbase.library:toolkit-ui-extension-consecutivescroller:$version
```

[Kotlin-Compiler-Ktx](https://github.com/MeowBase/toolkit/tree/master/kotlin-compiler) - 为 [KotlinCompiler](https://github.com/JetBrains/kotlin/tree/master/compiler) 增加易用的方法（PSI 解析 Kotlin 源文件等）
```kotlin
com.meowbase.library:toolkit-ktcompiler:$version
```