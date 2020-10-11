package com.rin.ui.samples.widget

import androidx.appcompat.widget.Toolbar
import com.mars.ui.Ui
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.dp
import com.mars.ui.currentTheme
import com.mars.ui.widget.With
import com.mars.ui.widget.modifier.background
import com.mars.ui.widget.modifier.elevation
import com.mars.ui.widget.modifier.safeContentArea


fun Ui.TitleBar(title: String) {
  With(
    creator = ::Toolbar,
    modifier = Modifier
      .safeContentArea(top = true)
      .elevation(24.dp)
      .background(currentTheme.colors.primary)
  ) {
    it.title = title
    it.setTitleTextColor(currentTheme.colors.onPrimary.argb)
  }
}