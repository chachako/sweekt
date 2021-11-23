package com.meowool.sweekt


import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode

/**
 * Determines whether this [KtAnnotationEntry] has the specified qualified name.
 * Careful: this does *not* currently take into account Kotlin type aliases (https://kotlinlang.org/docs/reference/type-aliases.html).
 *   Fortunately, type aliases are extremely uncommon for simple annotation types.
 */
private fun KtAnnotationEntry.fqNameMatches(fqName: FqName): Boolean {
  // For inspiration, see IDELightClassGenerationSupport.KtUltraLightSupportImpl.findAnnotation in the Kotlin plugin.
  val shortName = shortName?.asString() ?: return false
  return fqName.asString().endsWith(shortName) && fqName == getQualifiedName()
}

/**
 * Computes the qualified name of this [KtAnnotationEntry].
 * Prefer to use [fqNameMatches], which checks the short name first and thus has better performance.
 */
private fun KtAnnotationEntry.getQualifiedName(): FqName? =
  analyze(BodyResolveMode.PARTIAL).get(BindingContext.ANNOTATION, this)?.fqName


internal fun KtNamedDeclaration.isLazyInitProperty(): Boolean {
  return CachedValuesManager.getCachedValue(this) {
    cachedResult(annotationEntries.any { it.fqNameMatches(SweektNames.LazyInit) })
  }
}

private fun <T> KtNamedDeclaration.cachedResult(value: T) = CachedValueProvider.Result.create(
  // TODO: https://github.com/JetBrains/compose-jb/blob/master/idea-plugin/src/main/kotlin/org/jetbrains/compose/desktop/ide/preview/locationUtils.kt#L135:30
  value,
  this.containingKtFile,
  ProjectRootModificationTracker.getInstance(project)
)