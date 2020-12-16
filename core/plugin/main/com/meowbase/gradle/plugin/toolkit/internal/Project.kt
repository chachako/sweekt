@file:Suppress("PackageDirectoryMismatch")

import java.io.File

@InternalMeowbaseApi
val meowbasePath: String by lazy { System.getenv("MEOW_BASE") }

@InternalMeowbaseApi
val meowbaseDir: File by lazy { File(meowbasePath) }

@InternalMeowbaseApi
val meowbaseInternalLocalPath
  get() = "$meowbasePath/internal/"

@InternalMeowbaseApi
val meowbaseInternalLocalRepoPath
  get() = "$meowbaseInternalLocalPath.release/"