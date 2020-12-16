package com.rin.ui.samples.widget

import androidx.appcompat.widget.Toolbar
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.currentTheme
import com.meowbase.ui.widget.With
import com.meowbase.ui.widget.modifier.background
import com.meowbase.ui.widget.modifier.elevation
import com.meowbase.ui.widget.modifier.safeContentArea


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