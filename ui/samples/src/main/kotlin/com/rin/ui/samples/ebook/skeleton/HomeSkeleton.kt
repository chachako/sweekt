@file:Suppress("MemberVisibilityCanBePrivate")

package com.rin.ui.samples.ebook.skeleton

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import com.mars.toolkit.lifecycle.mutableLiveDataOf
import com.mars.ui.Theme
import com.mars.ui.Ui
import com.mars.ui.UIBody
import com.mars.ui.UiPreview
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.dp
import com.mars.ui.widget.*
import com.mars.ui.widget.modifier.*
import com.mars.ui.skeleton.Skeleton
import com.mars.ui.widget.implement.Text
import com.rin.ui.samples.ebook.Ebook

/**
 * 主界面骨架
 *
 * @author 凛
 * @github https://github.com/oh-Rin
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