@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.rin.ui.samples

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mars.toolkit.content.res.stringResource
import com.mars.ui.UIBody
import com.mars.ui.Ui
import com.mars.ui.UiPreview
import com.mars.ui.core.Alignment
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.unit.dp
import com.mars.ui.currentTheme
import com.mars.ui.skeleton.Skeleton
import com.mars.ui.skeleton.skeletalSystem
import com.mars.ui.widget.*
import com.mars.ui.widget.implement.Text
import com.mars.ui.widget.modifier.*
import com.rin.ui.samples.transition.TransitionsSkeleton
import com.rin.ui.samples.widget.TitleBar


/**
 * 一个用于启动 [NavigationSkeleton] 的活动
 *
 * @author 凛
 * @github https://github.com/oh-Rin
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
  var text: Text? = null
  var lifecycleList = ""

  override var uiBody: UIBody = {
    Column(crossAxisAlign = CrossAxisAlignment.Center) {
      TitleBar(title = stringResource(R.string.app_name))
      Column(Modifier.verticalScroll()
        .background(Color.Black.copy(0.05f))
        .padding(16.dp)
        .height(100.dp)
      ) {
        Text(
          "生命周期记录",
          color = currentTheme.colors.primaryVariant,
          style = currentTheme.typography.caption,
        )
        text = Text(lifecycleList)
      }
      ScrollableColumn(
        modifier = Modifier.paddingVertical(8.dp),
        crossAxisAlign = CrossAxisAlignment.Stretch
      ) {
        Case("动画", TransitionsSkeleton())
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
    text?.update(text = lifecycleList)
  }

  fun Ui.Case(name: String, skeleton: Skeleton) = Button(
    text = name,
    modifier = Modifier.margin(vertical = 8.dp, horizontal = 16.dp)
  ) { push(skeleton) }
}

@Deprecated(UiPreview.Deprecated)
private class NavigationPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, ::NavigationSkeleton)