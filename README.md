# Toolkit [ 🚧 Work in progress ⛏👷🔧️ 🚧 ]  ![Bintray](https://img.shields.io/bintray/v/oh-rin/meowbase/gradle-toolkit?color=B87CEF)


**已开源依赖**：

[Core-Plugin](/core/plugin) - 核心 Gradle 插件，可简化大量的 gradle.kts 工作：[示例](https://github.com/MeowBase/toolkit/blob/master/settings.gradle.kts#L36)
```kotlin
com.meowbase.plugin:toolkit:$version
```

[Core-Jvm](/core/jvm) - 适用于 JVM 平台的核心工具：[示例](/core/jvm/test)
```kotlin
com.meowbase.library:toolkit-core-jvm:$version
```

[Core-Android](/core/android) - 适用于 Android 平台的核心工具
```kotlin
com.meowbase.library:toolkit-core-android:$version
```

[Preference](/preference) - 适用于 Android 平台的 Key-Value 储存工具
```kotlin
// Preference 核心运行时代码 (默认为 SharedPreference 模型)，实现灵感来源于 [KotPref](https://github.com/chibatching/Kotpref)（必须）
com.meowbase.library:toolkit-preference-plugin:$version

// 对 [MMKV](https://github.com/Tencent/MMKV) 提供 Preference 模型的扩展支持 -> [MmkvModel](https://github.com/meowbasePlanning/toolkit/blob/master/preference/runtime/mmkv/native/sources/com/meowbase/preference/mmkv/MmkvModel.kt)
com.meowbase.library:toolkit-preference-plugin:$version

// 为所有 Preference 模型提供混淆支持（可选）
com.meowbase.library:toolkit-preference-plugin:$version
```

[UI-Kit](/ui) - 以 AndroidViews 衍生的一个强大的 UI 工具包：[示例](/ui/samples)
```kotlin
// UiKit 的核心 Gradle 插件（必须）
com.meowbase.plugin:toolkit:ui-plugin:$version

// UiKit 的核心运行时代码（必须）
com.meowbase.library:toolkit-ui-runtime:$version

// 提供 [Coil](https://github.com/coil-kt) 在 UiKit 中的增强
com.meowbase.library:toolkit-ui-extension-coil:$version

// 提供 [ConsecutiveScroller](https://github.com/donkingliang/ConsecutiveScroller) 在 UiKit 中的增强
com.meowbase.library:toolkit-ui-extension-consecutivescroller:$version
```

[Kotlin-Compiler-Ktx](/kotlin-compiler) - 为 [KotlinCompiler](https://github.com/JetBrains/kotlin/tree/master/compiler) 增加易用的方法（PSI 解析 Kotlin 源文件等）
```kotlin
com.meowbase.library:toolkit-ktcompiler:$version
```
