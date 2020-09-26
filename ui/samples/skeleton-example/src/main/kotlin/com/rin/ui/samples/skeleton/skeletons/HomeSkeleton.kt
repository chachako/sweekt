@file:Suppress("MemberVisibilityCanBePrivate")

package com.rin.ui.samples.skeleton.skeletons

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import com.mars.toolkit.content.res.stringResource
import com.mars.toolkit.lifecycle.mutableLiveDataOf
import com.mars.ui.UIBody
import com.mars.ui.Ui
import com.mars.ui.UiPreview
import com.mars.ui.core.Alignment
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.RoundedCornerShape
import com.mars.ui.core.unit.dp
import com.mars.ui.currentTheme
import com.mars.ui.foundation.Column
import com.mars.ui.foundation.Stack
import com.mars.ui.foundation.Text
import com.mars.ui.foundation.With
import com.mars.ui.foundation.modifies.*
import com.mars.ui.skeleton.Skeleton
import com.rin.ui.samples.skeleton.R

/*
 * author: 凛
 * date: 2020/9/24 下午12:12
 * github: https://github.com/oh-Rin
 * description: 主界面骨架
 */
class HomeSkeleton : Skeleton() {
  var text: Text? = null
  var lifecycleList = "当前状态"
  var lifecycleState = mutableLiveDataOf("")

  override var uiBody: UIBody = {
    Column(crossAxisAlign = CrossAxisAlignment.Center) {
      clipChildren = false
      clipToPadding = false
      TitleBar()
      text = Text(
        lifecycleList,
        modifier = Modifier.padding(16.dp),
        align = Alignment.CenterHorizontally
      )
//      Button(text = "打开一个 Skeleton", modifier = Modifier.shadow(Color.Black)) {
//
//      }
      Stack(
        modifier = Modifier.matchParent()
          .margin(top = 88.dp)
          .background(Color.Red).clip(RoundedCornerShape(topLeftPercent = 10, topRightPercent = 10))
      ) {

      }
    }
  }

  fun Ui.TitleBar() {
    With(
      creator = ::Toolbar,
      modifier = Modifier
        .safeContentArea(top = true)
        .background(currentTheme.colors.primary)
    ) {
      it.elevation = 24f
      it.title = stringResource(R.string.app_name)
      it.setTitleTextColor(currentTheme.colors.onPrimary.argb)
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
    lifecycleList += "\n" + state
    text?.update(text = lifecycleList)
  }
}

@Deprecated(UiPreview.Deprecated)
class HomeSkeletonPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, ::HomeSkeleton)