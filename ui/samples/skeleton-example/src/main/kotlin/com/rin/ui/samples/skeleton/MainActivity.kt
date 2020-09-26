@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.rin.ui.samples.skeleton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rin.ui.samples.skeleton.skeletons.HomeSkeleton
import com.mars.ui.skeleton.engine

/*
 * author: 凛
 * date: 2020/9/11 下午11:06
 * github: https://github.com/oh-Rin
 * description: 
 */
class MainActivity : AppCompatActivity() {
  val engine by engine(HomeSkeleton())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    engine.dispatchCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    engine.dispatchStart()
  }

  override fun onStop() {
    super.onStop()
    engine.dispatchStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    engine.dispatchDestroy()
  }
}