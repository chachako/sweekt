@file:Suppress("PackageDirectoryMismatch")

import java.io.File

@InternalMarsProjectApi
val marsProjectPath: String by lazy { System.getenv("MARS_PROJECT_ROOT") }

@InternalMarsProjectApi
val marsProjectDir: File by lazy { File(marsProjectPath) }

@InternalMarsProjectApi
val marsLocalInternalPath
  get() = "$marsProjectPath/internal/"

@InternalMarsProjectApi
val marsLocalInternalRepoPath
  get() = "$marsLocalInternalPath.release/"