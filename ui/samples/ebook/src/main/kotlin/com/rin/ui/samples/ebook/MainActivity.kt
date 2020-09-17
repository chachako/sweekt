@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.rin.ui.samples.ebook

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mars.toolkit.content.res.resource
import com.mars.ui.*
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation
import com.mars.ui.core.unit.dp
import com.mars.ui.extension.coil.CoilImage
import com.mars.ui.extension.coil.transforms.asTransformation
import com.mars.ui.extension.list.CommonRecyclableList
import com.mars.ui.extension.list.impl.data.dataSourceOf
import com.mars.ui.foundation.*
import com.mars.ui.foundation.modifies.*

/*
 * author: 凛
 * date: 2020/9/11 下午11:06
 * github: https://github.com/oh-Rin
 * description: 
 */
class MainActivity : AppCompatActivity(), UiKit.Preview {
  val horizontalEdge = 30.dp
  override var ui: UiScope = {
    ThemeScope(DarkTheme) {
      Screen()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setUiContent(content = ui)
  }

  fun UiKit.Screen() {
    Column(
      modifier = Modifier.safeContentArea(top = true)
        .background()
        .matchParent()
    ) {
      TitleBar()
      Banner()
    }
  }

  fun UiKit.TitleBar() {
    Row(
      crossAxisAlign = CrossAxisAlignment.Center,
      modifier = Modifier.padding(horizontal = horizontalEdge, vertical = 30.dp)
    ) {
      Image(R.drawable.ic_logo, modifier = Modifier.height(17.dp))
      Spacer()
      IconButton(
        resource(R.drawable.ic_search),
        modifier = Modifier.size(54.dp),
        iconSize = 24.dp,
      )
    }
  }

  fun UiKit.Banner() {
    CommonRecyclableList(
      dataSource = dataSourceOf(
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
        Book("https://inews.gtimg.com/newsapp_bt/0/9940432866/1000"),
      ),
      orientation = Orientation.Horizontal,
    ) {
//      val image = Image(
//        modifier = Modifier.height(224.dp),
//      )
//      Bind { book ->
//        image.loadAny(data = book.cover) {
//          transformations(Theme.shapes.medium.asTransformation)
//        }
//      }
      val image = CoilImage(
        lazy = true,
        transformation = Theme.shapes.medium.asTransformation,
        modifier = Modifier.height(224.dp),
      )
      Bind { book ->
        image.update(data = book.cover)
      }
    }
  }
}

@Deprecated("仅用于 IDE 编辑预览", level = DeprecationLevel.HIDDEN)
private class MainScreenPreview(context: Context, attrs: AttributeSet?) :
  UiPreview(context, attrs, MainActivity())