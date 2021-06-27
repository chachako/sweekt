package com.meowool.sweekt.datetime

import android.os.Build

/**
 * Returns the current instant from the system clock.
 */
actual val nowMilliseconds: Long
  get() = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    true -> nowInstant.epochMillis
    false -> System.currentTimeMillis()
  }