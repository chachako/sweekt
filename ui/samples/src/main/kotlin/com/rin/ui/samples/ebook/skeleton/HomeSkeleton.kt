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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.rin.ui.samples.ebook.skeleton

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import com.meowbase.toolkit.lifecycle.mutableLiveDataOf
import com.meowbase.ui.Theme
import com.meowbase.ui.Ui
import com.meowbase.ui.UIBody
import com.meowbase.ui.UiPreview
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.widget.*
import com.meowbase.ui.widget.modifier.*
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.widget.implement.Text
import com.rin.ui.samples.ebook.Ebook

/**
 * 主界面骨架
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/11 - 23:32
 */
class HomeSkeleton : Skeleton() {
  var text: Text? = null
  val horizontalEdge = 30.dp
  var lifecycleList = ""
  var lifecycleState = mutableLiveDataOf("")

  override val theme: Theme = Ebook.DarkTheme
  override val modifier: Modifier = Modifier.safeContentArea(top = true)
    .matchParent()
    .background()
  override var uiBody: UIBody = {
    Column {
      TitleBar()
      text = Text(lifecycleList)
    }
  }

  fun Ui.TitleBar() {
    Row(
      crossAxisAlign = CrossAxisAlignment.Center,
      modifier = Modifier.padding(horizontal = horizontalEdge, vertical = 30.dp)
    ) {
//      Image(R.drawable.ic_logo, modifier = Modifier.height(17.dp))
//      Spacer()
//      IconButton(
//        resource(R.drawable.ic_search),
//        modifier = Modifier.size(54.dp),
//        iconSize = 24.dp,
//      )
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appendLifecycleState("onCreate")
  }

  override fun onAppear() {
    super.onAppear()
    appendLifecycleState("onAppear")
  }

  override fun onDisappear() {
    super.onDisappear()
    appendLifecycleState("onDisappear")
  }

  fun appendLifecycleState(state: String) {
    lifecycleList += "\n" + state
    text?.update(text = lifecycleList)
  }
}

@Deprecated(UiPreview.Deprecated)
class HomeSkeletonPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, ::HomeSkeleton)