package com.meowbase.gradle.plugin.toolkit.ktx

import org.gradle.api.Task
import java.io.File

val Task.inputFileSet: Set<File> get() = inputs.files.files

val Task.outputFileSet: Set<File> get() = outputs.files.files