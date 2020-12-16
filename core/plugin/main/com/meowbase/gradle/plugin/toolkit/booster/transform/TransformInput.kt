package com.meowbase.gradle.plugin.toolkit.booster.transform

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.TransformOutputProvider
import java.io.File

class TransformInput(
  private val name: String,
  private val contentTypes: Set<ContentType>,
  private val scopes: MutableSet<in Scope>,
  private val format: Format
) {
  constructor(di: DirectoryInput) : this(di.name, di.contentTypes, di.scopes, Format.DIRECTORY)
  constructor(ji: JarInput, name: String) : this(name, ji.contentTypes, ji.scopes, Format.JAR)

  fun toOutput(outputProvider: TransformOutputProvider): File =
    outputProvider.getContentLocation(
      name,
      contentTypes,
      scopes,
      format
    )
}
