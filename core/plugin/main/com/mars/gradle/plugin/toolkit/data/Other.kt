@file:Suppress("unused", "SpellCheckingInspection", "UnstableApiUsage", "PackageDirectoryMismatch")

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.Variant
import com.android.build.api.variant.VariantProperties
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import com.android.build.gradle.internal.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.SigningConfig
import org.gradle.api.Project
import java.io.File
import java.util.*

/** 混合的 extension，适用于 app/aar */
internal typealias MixinExtension = CommonExtension<AndroidSourceSet,
  BuildFeatures,
  BuildType,
  DefaultConfig,
  ProductFlavor,
  SigningConfig,
  Variant<VariantProperties>,
  VariantProperties>

/** 读取此项目的 local.properties */
val Project.localPropertiesOrNull: Properties?
  get() = File(projectDir, "local.properties").propertiesOrNull
val Project.localProperties: Properties
  get() = localPropertiesOrNull!!

/** 读取根项目的 local.properties */
val Project.rootLocalPropertiesOrNull: Properties?
  get() = rootDir.resolve("local.properties").propertiesOrNull
val Project.rootLocalProperties: Properties
  get() = rootLocalPropertiesOrNull!!

/**
 * 从 local.properties 中读取 key 信息
 * NOTE: 如果是内部开发会先获取环境变量中的 key.properties
 */
@InternalMarsProjectApi
internal val Project.keyProperties
  get() = File(marsLocalInternalPath, ".key/key.properties").propertiesOrNull
    ?: localPropertiesOrNull
    ?: rootLocalProperties


private val File.propertiesOrNull: Properties?
  get() = if (exists()) Properties().apply {
    load(this@propertiesOrNull.bufferedReader())
  } else null