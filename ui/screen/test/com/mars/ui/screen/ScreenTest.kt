@file:Suppress("RedundantOverride", "unused", "TestFunctionName")

package com.mars.ui.screen

import android.view.View
import com.mars.toolkit.graphics.asBitmap
import com.mars.toolkit.graphics.asDrawable
import com.mars.ui.Ui
import com.mars.ui.UiBody
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.dp
import com.mars.ui.foundation.*
import com.mars.ui.foundation.modifies.margin
import java.io.File

/*
 * author: 凛
 * date: 2020/9/21 下午6:20
 * github: https://github.com/oh-Rin
 */
class ScreenTest : UiScreen() {
  override fun Ui.Body() {
    Column {
      TopAppBar {
        Text("Ui-Sample")
      }
      Content()
    }
  }

  private fun Ui.TopAppBar(Title: UiBody) {
    Row(crossAxisAlign = CrossAxisAlignment.Center) {
      Icon(asset = File("back.png").asDrawable)
      Title()
      Spacer()
      Icon(asset = File("menu.png").asDrawable)
    }
  }

  private fun Ui.Content() {
    Column {
      Text("Hello Ui.")
      Image(
        asset = android.R.drawable.gallery_thumb,
        modifier = Modifier.margin(top = 24.dp)
      )
    }
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
  }

}