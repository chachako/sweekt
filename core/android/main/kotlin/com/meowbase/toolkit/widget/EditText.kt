package com.meowbase.toolkit.widget

import android.text.InputFilter
import android.widget.EditText
import com.meowbase.toolkit.NoGetter
import com.meowbase.toolkit.noGetter
import com.meowbase.toolkit.text.InputType

inline var EditText.type: InputType<*>
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(newType) {
    inputType = newType.value
  }

inline var EditText.maxTextLength: Int
  @Deprecated(NoGetter, level = DeprecationLevel.HIDDEN) get() = noGetter
  set(value) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(value))
  }