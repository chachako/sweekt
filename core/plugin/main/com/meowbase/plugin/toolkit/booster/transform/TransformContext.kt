/*
 * Copyright (c) 2021. Rin Orz (凛)
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
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.plugin.toolkit.booster.transform

import java.io.File

/**
 * Represent the transform context
 *
 * @author johnsonlee
 */
interface TransformContext {

  /**
   * The name of transform
   */
  val name: String

  /**
   * The project directory
   */
  val projectDir: File

  /**
   * The build directory
   */
  val buildDir: File

  /**
   * The temporary directory
   */
  val temporaryDir: File

  /**
   * The reports directory
   */
  val reportsDir: File

  /**
   * The boot classpath
   */
  val bootClasspath: Collection<File>

  /**
   * The compile classpath
   */
  val compileClasspath: Collection<File>

  /**
   * The runtime classpath
   */
  val runtimeClasspath: Collection<File>

  /**
   * The class pool
   */
  val klassPool: KlassPool

  /**
   * The application identifier
   */
  val applicationId: String

  /**
   * The buildType is debuggable
   */
  val isDebuggable: Boolean

  /**
   * is dataBinding enabled or not
   */
  val isDataBindingEnabled: Boolean

  /**
   * Check if has the specified property. Generally, the property is equivalent to project property
   *
   * @param name the name of property
   */
  fun hasProperty(name: String): Boolean

  /**
   * Returns the value of the specified property. Generally, the property is equivalent to project property
   *
   * @param name the name of property
   * @param default the default value
   */
  fun <T> getProperty(name: String, default: T): T = default

}
