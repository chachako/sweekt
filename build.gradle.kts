/*
 * Copyright (c) 2022. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress

val internalMarkers = arrayOf(
  "com.meowool.sweekt.InternalSweektApi"
)

apiValidation {
  nonPublicMarkers += internalMarkers
}

allprojects {
  optIn(*internalMarkers)
  dokka(DokkaFormat.Html) {
    outputDirectory.set(rootDir.resolve("docs/apis"))
  }
}

publication.data {
  val baseVersion = "0.1.0"
  version = "$baseVersion-LOCAL"
  // Used to publish non-local versions of artifacts in CI environment
  versionInCI = "$baseVersion-SNAPSHOT"

  displayName = "Sweekt"
  groupId = "com.meowool.toolkit"
  description = "A common toolkit (utils) built to help you further reduce Kotlin boilerplate code and improve development efficiency."
  url = "https://github.com/meowool-toolkit/${rootProject.name}"
  vcs = "$url.git"
  developer {
    id = "rin"
    name = "Rin Orz"
    url = "https://github.com/RinOrz/"
  }
}
