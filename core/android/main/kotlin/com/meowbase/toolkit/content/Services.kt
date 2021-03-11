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

@file:Suppress("unused", "DEPRECATION")

package com.meowbase.toolkit.content

import android.Manifest
import android.app.*
import android.app.job.JobScheduler
import android.content.ClipData
import android.content.ClipboardManager
import android.hardware.SensorManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.*
import android.os.storage.StorageManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.meowbase.toolkit.appContext
import com.meowbase.toolkit.appData

inline val windowManager: WindowManager get() = systemService()
inline val clipboardManager: ClipboardManager get() = systemService()
inline val layoutInflater: LayoutInflater get() = systemService()
inline val activityManager: ActivityManager get() = systemService()
inline val powerManager: PowerManager get() = systemService()
inline val alarmManager: AlarmManager get() = systemService()
inline val notificationManager: NotificationManager get() = systemService()
inline val keyguardManager: KeyguardManager get() = systemService()
inline val locationManager: LocationManager get() = systemService()
inline val searchManager: SearchManager get() = systemService()
inline val storageManager: StorageManager get() = systemService()
inline val vibrator: Vibrator get() = systemService()
inline val connectivityManager: ConnectivityManager get() = systemService()
inline val wifiManager: WifiManager get() = systemService()
inline val audioManager: AudioManager get() = systemService()
inline val mediaRouter: MediaRouter get() = systemService()
inline val telephonyManager: TelephonyManager get() = systemService()
inline val sensorManager: SensorManager get() = systemService()
inline val subscriptionManager: SubscriptionManager get() = systemService()
inline val carrierConfigManager: CarrierConfigManager get() = systemService()
inline val inputMethodManager: InputMethodManager get() = systemService()
inline val uiModeManager: UiModeManager get() = systemService()
inline val downloadManager: DownloadManager get() = systemService()
inline val batteryManager: BatteryManager get() = systemService()
inline val jobScheduler: JobScheduler get() = systemService()
inline val accessibilityManager: AccessibilityManager get() = systemService()

/**
 * 根据类型 [T] 返回当前 App 上下文的系统服务
 *
 * @see ContextCompat.getSystemService
 */
inline fun <reified T : Any> systemService(): T =
  systemServiceOrNull()!!

inline fun <reified T : Any> systemServiceOrNull(): T? =
  appContext.getSystemService()

inline fun <reified T : Any> Class<*>.systemService(): T =
  ContextCompat.getSystemService(appContext, this) as T


/**
 * 启动手机的振动服务
 *
 * @param time 持续时间
 * @param amplitude 振动幅度，需要 Api 大于 [Build.VERSION_CODES.O]
 * @see vibrator
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun startVibration(time: Long, @IntRange(from = -1, to = 255) amplitude: Int = -1) =
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
    vibrator.vibrate(time)
  } else {
    vibrator.vibrate(VibrationEffect.createOneShot(time, amplitude))
  }

/**
 * 启动剪贴板服务，并将字符串复制到剪贴板
 *
 * @param label 剪贴的主标签
 * @see clipboardManager
 */
fun String.copyToClipboard(label: String = appData.packageName) {
  val clipData = ClipData.newPlainText(label, this)
  clipboardManager.setPrimaryClip(clipData)
}