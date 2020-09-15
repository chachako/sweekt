package com.mars.toolkit.app

import android.app.Activity
import android.view.View
import com.mars.toolkit.NoGetter
import com.mars.toolkit.noGetter
import kotlin.DeprecationLevel.HIDDEN

inline var Activity.contentView: View
  @Deprecated(NoGetter, level = HIDDEN) get() = noGetter
  set(value) = setContentView(value)
