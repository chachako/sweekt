package com.meowbase.ktcompiler.psi

import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtPsiFactory

inline val Project.ktPsiFactory: KtPsiFactory get() = ktPsiFactory()

fun Project.ktPsiFactory(markGenerated: Boolean = false): KtPsiFactory =
  KtPsiFactory(this, markGenerated)