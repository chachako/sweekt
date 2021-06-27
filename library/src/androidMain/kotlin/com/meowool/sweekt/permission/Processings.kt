package com.meowool.sweekt.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.meowool.sweekt.asActivity
import com.meowool.sweekt.cast
import com.meowool.sweekt.start


/**
 * Returns the grant status of all given [permissions].
 */
fun Context.getPermissionStatus(vararg permissions: Permission): Map<Permission.Single, Boolean> =
  permissions
    .flatMap { it.value }
    .map { Permission(it) to (checkSelfPermission(this, it) == PERMISSION_GRANTED) }
    .toMap()

/**
 * Determine whether the context has granted all given [permissions].
 *
 * @return returns `true` if all given [permissions] has been granted.
 */
fun Context.isGrantedPermissions(vararg permissions: Permission): Boolean =
  permissions.flatMap { it.value }.all { checkSelfPermission(this, it) == PERMISSION_GRANTED }

/**
 * Request the permission (group) by `androidx.activity.result`.
 *
 * @receiver the context to request permission.
 * @param granted invoke the callback and return all granted permissions.
 * @param denied invoke the callback and return all denied permissions.
 */
fun Context.requestPermissions(
  vararg permissions: Permission,
  granted: Permission.Callback = Permission.Callback {},
  denied: Permission.Callback = Permission.Callback {},
) = synchronized(this) {
  permissions.forEach { permission ->
    try {
      this.asActivity().cast<ComponentActivity>()
        .registerForActivityResult(RequestMultiplePermissions()) { result ->
          val grantedPermissions = result.filterValues { it }.keys
          if (grantedPermissions.isNotEmpty()) granted(grantedPermissions)

          val deniedPermissions = result.filterValues { it.not() }.keys
          if (deniedPermissions.isNotEmpty()) denied(grantedPermissions)
        }.launch(permission.value.toTypedArray())
    } catch (e: Exception) {
      grantedCallback = granted
      deniedCallback = denied
      requestPermissions = permission.value
      this.start<PermissionCompatActivity>()
    }
  }
}

/**
 * Run [action] with all [permissions] granted.
 */
inline fun Context.runWithPermissions(
  vararg permissions: Permission,
  crossinline action: () -> Unit
) = when {
  isGrantedPermissions(*permissions) -> action()
  else -> requestPermissions(*permissions) { action() }
}