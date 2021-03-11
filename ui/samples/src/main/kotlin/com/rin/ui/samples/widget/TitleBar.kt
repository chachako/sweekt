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