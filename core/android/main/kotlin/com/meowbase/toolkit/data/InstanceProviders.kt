@file:Suppress("unused")

package com.meowbase.toolkit.data

import android.app.Activity
import android.content.Context

interface ContextProvider {
  fun provideContext(): Context
}

interface ActivityProvider {
  fun provideActivity(): Activity
}
