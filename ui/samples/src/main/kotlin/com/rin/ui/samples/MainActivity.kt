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

@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.rin.ui.samples

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.meowbase.toolkit.content.res.stringResource
import com.meowbase.ui.UIBody
import com.meowbase.ui.Ui
import com.meowbase.ui.UiPreview
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.core.unit.sp
import com.meowbase.ui.currentTheme
import com.meowbase.ui.skeleton.Skeleton
import com.meowbase.ui.skeleton.skeletalSystem
import com.meowbase.ui.widget.*
import com.meowbase.ui.widget.implement.ScrollableColumn
import com.meowbase.ui.widget.implement.Text
import com.meowbase.ui.widget.modifier.*
import com.rin.ui.samples.transition.TransitionsSkeleton
import com.rin.ui.samples.widget.TitleBar


/**
 * 一个用于启动 [NavigationSkeleton] 的活动
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/12 - 00:16
 */
class MainActivity : AppCompatActivity() {
  val skeletalSystem by skeletalSystem(NavigationSkeleton())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(skeletalSystem)
    skeletalSystem.dispatchCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    skeletalSystem.dispatchStart()
  }

  override fun onStop() {
    super.onStop()
    skeletalSystem.dispatchStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    skeletalSystem.dispatchDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    skeletalSystem.dispatchSaveInstanceState(outState)
  }

  override fun onBackPressed() {
    if (!skeletalSystem.interceptBackPressed()) {
      super.onBackPressed()
    }
  }
}

/**
 * 一个用于导航所有 [Skeleton] 示例的骨架
 */
class NavigationSkeleton : Skeleton() {
  lateinit var text: Text
  lateinit var scrollableRecord: ScrollableColumn
  var lifecycleList = ""

  override var uiBody: UIBody = {
    Column(crossAxisAlign = CrossAxisAlignment.Center) {
      TitleBar(title = stringResource(R.string.app_name))
      scrollableRecord = ScrollableColumn(
        modifier = Modifier
          .background(Color.Black.copy(0.05f))
          .padding(16.dp)
          .height(106.dp)
      ) {
        Text(
          "生命周期记录",
          color = currentTheme.colors.primaryVariant,
          style = currentTheme.typography.caption,
        )
        Spacer(height = 8.dp)
        text = Text(lifecycleList)
      }
      ScrollableColumn(
        modifier = Modifier.paddingVertical(8.dp),
        crossAxisAlign = CrossAxisAlignment.Stretch
      ) {
        CaseGroup("动画案例")
        Case("转场动画", ::TransitionsSkeleton)
        CaseGroup("控件展示")
        Case("Google Material 主题", ::TransitionsSkeleton)
        Case("iOS 主题", ::TransitionsSkeleton)
        CaseGroup("App 案例")
        Case("Ebook App", ::TransitionsSkeleton)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
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
    lifecycleList += if (lifecycleList.isEmpty()) state else ", $state"
    text.update(text = lifecycleList)
    // 每次更新确保滚动到最后一行
    scrollableRecord.smoothScrollTo(0, scrollableRecord.bottom)
  }

  fun Ui.Case(name: String, skeleton: () -> Skeleton) = Button(
    text = name,
    modifier = Modifier.margin(vertical = 8.dp, horizontal = 16.dp)
  ) { push(skeleton()) }

  fun Ui.CaseGroup(title: String) = Row(crossAxisAlign = CrossAxisAlignment.Center) {
    Spacer(Modifier.weight(1).height(0.5.dp).background(Color.Black.copy(0.15f)))
    Text(
      title,
      Modifier.padding(14.dp),
      style = currentTheme.typography.caption.copy(
        fontSize = 11.sp,
        color = currentTheme.colors.primary
      )
    )
    Spacer(Modifier.weight(1).height(0.5.dp).background(Color.Black.copy(0.15f)))
  }
}

@Deprecated(UiPreview.Deprecated)
private class NavigationPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, ::NavigationSkeleton)