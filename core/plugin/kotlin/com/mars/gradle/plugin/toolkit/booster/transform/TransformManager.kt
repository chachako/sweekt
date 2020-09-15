package com.mars.gradle.plugin.toolkit.booster.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet

val SCOPE_PROJECT: MutableSet<in QualifiedContent.Scope> = TransformManager.PROJECT_ONLY

val SCOPE_FULL_PROJECT: MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

val SCOPE_FULL_WITH_FEATURES: MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_WITH_FEATURES

val SCOPE_FULL_LIBRARY_WITH_FEATURES: MutableSet<in QualifiedContent.Scope> = ImmutableSet.Builder<QualifiedContent.ScopeType>()
  .addAll(TransformManager.SCOPE_FEATURES)
  .add(QualifiedContent.Scope.PROJECT)
  .build()