package com.meowbase.toolkit.app

import android.app.Activity
import android.view.View
import com.meowbase.toolkit.NoGetter
import com.meowbase.toolkit.noGetter
import kotlin.DeprecationLevel.HIDDEN

inline var Activity.contentView: View
  @Deprecated(NoGetter, level = HIDDEN) get() = noGetter
  set(value) = setContentView(value)
