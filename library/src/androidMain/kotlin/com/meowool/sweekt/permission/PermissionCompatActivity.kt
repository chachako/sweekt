package com.meowool.sweekt.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.meowool.sweekt.InternalSweektApi

/**
 * Bridge to communicate with permission requests
 *
 * @author 凛 (RinOrz)
 */
@InternalSweektApi
class PermissionCompatActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    overridePendingTransition(0, 0)
    requestPermissions?.toTypedArray()?.let {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(it, RequestCode)
      }
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == RequestCode) {
      val grantedPermissions = mutableSetOf<String>()
      val deniedPermissions = mutableSetOf<String>()

      grantResults.forEachIndexed { index, result ->
        val permission = permissions[index]
        when (result) {
          PackageManager.PERMISSION_GRANTED -> grantedPermissions += permission
          PackageManager.PERMISSION_DENIED -> deniedPermissions += permission
        }
      }

      grantedCallback?.invoke(grantedPermissions)
      deniedCallback?.invoke(deniedPermissions)
    }
    finish()
  }

  override fun onDestroy() {
    super.onDestroy()
    grantedCallback = null
    deniedCallback = null
    requestPermissions = null
  }
}

@PublishedApi internal var grantedCallback: Permission.Callback? = null
@PublishedApi internal var deniedCallback: Permission.Callback? = null
@PublishedApi internal var requestPermissions: List<String>? = null

private const val RequestCode = 3