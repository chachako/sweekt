@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate", "SpellCheckingInspection",
  "NON_EXHAUSTIVE_WHEN"
)

package com.dfkkextra.updater

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.ui.tooling.preview.Preview
import com.dfkkextra.updater.api.ApiManager.Api
import com.dfkkextra.updater.theme.UpdaterTheme
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mars.toolkit.content.windowView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/*
 * author: 凛
 * date: 2020/9/11 下午11:06
 * github: https://github.com/oh-Rin
 */
class MainActivity : AppCompatActivity() {
  private lateinit var container: FrameLayout
  private var snackbarActionColor: Color? = null
  private val list by lazy {
    listOf(
      Item(label = "备份参数", hint = "Target file name") {
        Api.call(
          mapOf(
            "fun" to "backup_params",
            "to_filename" to it as String
          ),
        ).code
      },
      Item(label = "还原参数", hint = "From file name") {
        Api.call(
          mapOf(
            "fun" to "restore_params",
            "from_filename" to it as String
          ),
        ).code
      },
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { Screen() }
  }

  @Preview(name = "Updater")
  @Composable
  private fun Screen() {
    UpdaterTheme {
      snackbarActionColor = MaterialTheme.colors.secondary
      Scaffold(topBar = { TopAppBar() }) {
        Content()
        Box(gravity = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
          AndroidView(viewBlock = ::FrameLayout) { container = it }
        }
      }
    }
  }

  @Composable
  private fun TopAppBar() {
    TopAppBar(
      title = {
        Text(
          text = stringResource(id = R.string.app_name),
          modifier = Modifier.fillMaxWidth()
        )
      },
      navigationIcon = {
        IconButton(onClick = ::onBackPressed) {
          Icon(Icons.Filled.ArrowBack)
        }
      },
      contentColor = MaterialTheme.colors.primary,
      backgroundColor = MaterialTheme.colors.background,
    )
  }

  @Composable
  private fun Content() {
    val coroutineScope = rememberCoroutineScope()
    val editValue = remember { mutableStateOf("") }

    LazyColumnFor(
      items = list,
      modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
    ) {
      var onClick: (() -> Unit)? = null; onClick = {
        coroutineScope.launch(it.dispatcher) {
          when (it.action(editValue.value)) {
            200 -> showSnackbar("执行成功")
            else -> showSnackbar("执行失败", action = { onClick!!() })
          }
        }
      }

      Row(
        verticalGravity = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 24.dp).fillParentMaxWidth()
      ) {
        OutlinedTextField(
          value = editValue.value,
          label = { Text(it.hint ?: "Option") },
          onValueChange = { editValue.value = if (it.endsWith(".txt")) it else "$it.txt" },
          modifier = Modifier.weight(1f).padding(start = 24.dp),
        )
        TextButton(
          enabled = editValue.value.isNotEmpty(),
          modifier = Modifier.padding(start = 12.dp, end = 12.dp),
          onClick = onClick,
        ) {
          Text(text = it.label, fontSize = 14.sp)
        }
      }
    }
  }

  private fun showSnackbar(label: String, action: ((View) -> Unit)? = null) {
    val snackbar = Snackbar.make(container, label, BaseTransientBottomBar.LENGTH_LONG)
    if (action != null) snackbar.setAction("重试") {
      snackbar.dismiss()
      action.invoke(it)
    }.setActionTextColor(snackbarActionColor!!.toArgb())
    snackbar.show()
  }

  data class Item(
    val label: String,
    val hint: String? = null,
    val dispatcher: CoroutineContext = Dispatchers.IO,
    val action: suspend (Any) -> Int
  )
}