package com.mars.ui.screen

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

internal class ScreenUiLifecycleOwner : SavedStateRegistryOwner {
  private var mLifecycleRegistry: LifecycleRegistry? = null
  private var mSavedStateRegistryController: SavedStateRegistryController? = null

  /**
   * Return true if the Lifecycle has been initialized.
   */
  val isInitialized: Boolean
    get() = mLifecycleRegistry != null

  /**
   * Initializes the underlying Lifecycle if it hasn't already been created.
   */
  fun initialize() {
    if (mLifecycleRegistry == null) {
      mLifecycleRegistry = LifecycleRegistry(this)
      mSavedStateRegistryController = SavedStateRegistryController.create(this)
    }
  }

  fun setCurrentState(state: Lifecycle.State) {
    mLifecycleRegistry!!.currentState = state
  }

  fun handleLifecycleEvent(event: Lifecycle.Event) {
    mLifecycleRegistry!!.handleLifecycleEvent(event)
  }

  fun performRestore(savedState: Bundle?) {
    mSavedStateRegistryController!!.performRestore(savedState)
  }

  fun performSave(outBundle: Bundle) {
    mSavedStateRegistryController!!.performSave(outBundle)
  }

  override fun getLifecycle(): Lifecycle {
    initialize()
    return mLifecycleRegistry!!
  }

  override fun getSavedStateRegistry(): SavedStateRegistry {
    return mSavedStateRegistryController!!.savedStateRegistry
  }
}