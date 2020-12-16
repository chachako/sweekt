package com.meowbase.gradle.plugin.toolkit.booster.transform

/**
 * Represents the transform lifecycle listener
 *
 * @author johnsonlee
 */
interface TransformListener {

  fun onPreTransform(context: TransformContext) {}

  fun onPostTransform(context: TransformContext) {}

}