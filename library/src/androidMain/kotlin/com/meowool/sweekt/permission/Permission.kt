@file:Suppress("SpellCheckingInspection", "FunctionName", "DEPRECATION")

package com.meowool.sweekt.permission

import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.Manifest.permission.ADD_VOICEMAIL
import android.Manifest.permission.ANSWER_PHONE_CALLS
import android.Manifest.permission.BODY_SENSORS
import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.CAMERA
import android.Manifest.permission.GET_ACCOUNTS
import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.Manifest.permission.PROCESS_OUTGOING_CALLS
import android.Manifest.permission.READ_CALENDAR
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.READ_SMS
import android.Manifest.permission.RECEIVE_MMS
import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.RECEIVE_WAP_PUSH
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.SEND_SMS
import android.Manifest.permission.USE_SIP
import android.Manifest.permission.WRITE_CALENDAR
import android.Manifest.permission.WRITE_CALL_LOG
import android.Manifest.permission.WRITE_CONTACTS
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageInstaller
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.Q
import androidx.annotation.RequiresApi
import com.meowool.sweekt.permission.Permission.ActivityRecognition
import com.meowool.sweekt.permission.Permission.AddVoicemail
import com.meowool.sweekt.permission.Permission.Calendar
import com.meowool.sweekt.permission.Permission.CallLog
import com.meowool.sweekt.permission.Permission.Camera
import com.meowool.sweekt.permission.Permission.Contacts
import com.meowool.sweekt.permission.Permission.GetAccounts
import com.meowool.sweekt.permission.Permission.Location
import com.meowool.sweekt.permission.Permission.Microphone
import com.meowool.sweekt.permission.Permission.NearbyDevices
import com.meowool.sweekt.permission.Permission.Phone
import com.meowool.sweekt.permission.Permission.ProcessOutgoingCalls
import com.meowool.sweekt.permission.Permission.ReceiveMMS
import com.meowool.sweekt.permission.Permission.ReceiveWapPush
import com.meowool.sweekt.permission.Permission.SMS
import com.meowool.sweekt.permission.Permission.Sensors
import com.meowool.sweekt.permission.Permission.Storage
import com.meowool.sweekt.permission.Permission.UseSIP


/**
 * Permission declaration and request.
 *
 * @see Permission.Callback
 * @see Context.isGrantedPermissions
 * @see Context.requestPermissions
 * @see Context.runWithPermissions
 *
 * @author 凛 (https://github.com/RinOrz)
 */
sealed class Permission(@PublishedApi internal val value: List<String>) {
  constructor(vararg value: String) : this(value.toList())
  constructor(vararg value: Permission) : this(value.flatMap { it.value })

  open class Single(value: String) : Permission(value)

  /**
   * Allows an application to recognize physical activity.
   *
   * For `AndroidManifest.xml`: `android.permission.ACTIVITY_RECOGNITION`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#ACTIVITY_RECOGNITION)
   */
  @RequiresApi(Q) object ActivityRecognition : Single(ACTIVITY_RECOGNITION)

  /**
   * Used for runtime permissions related to user's calendar.
   *
   * @see Calendar.Read
   * @see Calendar.Write
   */
  object Calendar : Permission(Read, Write) {

    /**
     * Allows an application to read the user's calendar data.
     *
     * For `AndroidManifest.xml`: `android.permission.READ_CALENDAR`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_CALENDAR)
     */
    object Read : Single(READ_CALENDAR)

    /**
     * Allows an application to write the user's calendar data.
     *
     * For `AndroidManifest.xml`: `android.permission.WRITE_CALENDAR`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#WRITE_CALENDAR)
     */
    object Write : Single(WRITE_CALENDAR)
  }

  /**
   * Used for permissions that are associated telephony features.
   *
   * @see CallLog.Read
   * @see CallLog.Write
   */
  object CallLog : Permission(Read, Write) {

    /**
     * Allows an application to read the user's call log.
     *
     * Note: If your app uses the [Contacts.Read] permission and both your minSdkVersion and
     * targetSdkVersion values are set to 15 or lower, the system implicitly grants your app this
     * permission. If you don't need this permission, be sure your targetSdkVersion is 16 or higher.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.READ_CALL_LOG`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_CALL_LOG)
     */
    object Read : Single(READ_CALL_LOG)

    /**
     * Allows an application to write (but not read) the user's call log data.
     *
     * Note: If your app uses the [Contacts.Write] permission and both your minSdkVersion and
     * targetSdkVersion values are set to 15 or lower, the system implicitly grants your app this
     * permission. If you don't need this permission, be sure your targetSdkVersion is 16 or higher.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.WRITE_CALL_LOG`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#WRITE_CALL_LOG)
     */
    object Write : Single(WRITE_CALL_LOG)
  }

  /**
   * Allows accessing camera or capturing images/video from the device.
   *
   * This will automatically enforce the uses-feature manifest element for all camera features.
   * If you do not require all camera features or can properly operate if a camera is not available,
   * then you must modify your manifest as appropriate in order to install on devices that don't
   * support all camera features.
   *
   * For `AndroidManifest.xml`: `android.permission.CAMERA`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#CAMERA)
   */
  object Camera : Single(CAMERA)

  /**
   * Used for runtime permissions related to contacts and profiles on this device.
   *
   * @see Contacts.Read
   * @see Contacts.Write
   * @see GetAccounts
   */
  object Contacts : Permission(Read, Write, GetAccounts) {

    /**
     * Allows an application to read the user's contacts data.
     *
     * For `AndroidManifest.xml`: `android.permission.READ_CONTACTS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_CONTACTS)
     */
    object Read : Single(READ_CONTACTS)

    /**
     * Allows an application to write the user's contacts data.
     *
     * For `AndroidManifest.xml`: `android.permission.WRITE_CONTACTS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#WRITE_CONTACTS)
     */
    object Write : Single(WRITE_CONTACTS)
  }

  /**
   * Allows an application to write the user's contacts data.
   *
   * Note: Beginning with Android 6.0 (API level 23), if an app shares the signature of the
   * authenticator that manages an account, it does not need [GET_ACCOUNTS] permission to read
   * information about that account. On Android 5.1 and lower, all apps need [GET_ACCOUNTS]
   * permission to read information about any account.
   *
   * For `AndroidManifest.xml`: `android.permission.GET_ACCOUNTS`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#GET_ACCOUNTS)
   */
  object GetAccounts : Single(GET_ACCOUNTS)

  /**
   * Used for permissions that allow accessing the device location.
   *
   * @see Location.AccessCoarse
   * @see Location.AccessFine
   * @see Location.AccessBackground
   * @see Location.AccessMedia
   */
  object Location : Permission(AccessCoarse, AccessFine, AccessBackground) {

    /**
     * Allows an app to access approximate location. Alternatively, you might want [AccessFine].
     *
     * For `AndroidManifest.xml`: `android.permission.ACCESS_COARSE_LOCATION`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#ACCESS_COARSE_LOCATION)
     */
    object AccessCoarse : Single(ACCESS_COARSE_LOCATION)

    /**
     * Allows an app to access precise location. Alternatively, you might want [AccessCoarse].
     *
     * For `AndroidManifest.xml`: `android.permission.ACCESS_FINE_LOCATION`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION)
     */
    object AccessFine : Single(ACCESS_FINE_LOCATION)

    /**
     * Allows an app to access location in the background. If you're requesting this permission, you
     * must also request either [AccessCoarse] or [AccessFine]. Requesting this
     * permission by itself doesn't give you location access.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.ACCESS_BACKGROUND_LOCATION`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#ACCESS_BACKGROUND_LOCATION)
     */
    @RequiresApi(Q) object AccessBackground : Single(ACCESS_BACKGROUND_LOCATION)

    /**
     * Allows an application to access any geographic locations persisted in the user's
     * shared collection.
     *
     * For `AndroidManifest.xml`: `android.permission.ACCESS_MEDIA_LOCATION`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#ACCESS_MEDIA_LOCATION)
     */
    @RequiresApi(Q) object AccessMedia : Single(ACCESS_MEDIA_LOCATION)
  }

  /**
   * Allows accessing microphone audio from the device.
   *
   * For `AndroidManifest.xml`: `android.permission.RECORD_AUDIO`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#RECORD_AUDIO)
   * [Refer](https://developer.android.com/reference/android/Manifest.permission_group#MICROPHONE)
   */
  object Microphone : Single(RECORD_AUDIO)

  /**
   * TODO Change to Android S constants.
   * Required to be able to discover and connect to nearby Bluetooth devices.
   *
   * @see NearbyDevices.BluetoothConnect
   * @see NearbyDevices.BluetoothScan
   */
  @RequiresApi(31) object NearbyDevices : Permission(BluetoothConnect, BluetoothScan) {

    /**
     * Required to be able to connect to paired Bluetooth devices.
     *
     * For `AndroidManifest.xml`: `android.permission.BLUETOOTH_CONNECT`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#BLUETOOTH_CONNECT)
     */
    @RequiresApi(31) object BluetoothConnect : Single("android.permission.BLUETOOTH_CONNECT")

    /**
     * Required to be able to discover and pair nearby Bluetooth devices.
     *
     * For `AndroidManifest.xml`: `android.permission.BLUETOOTH_SCAN`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#BLUETOOTH_SCAN)
     */
    @RequiresApi(31) object BluetoothScan : Single("android.permission.BLUETOOTH_SCAN")
  }

  /**
   * Used for permissions that allow accessing the device location.
   *
   * @see Phone.Call
   * @see Phone.ReadState
   * @see Phone.ReadNumber
   * @see Phone.AnswerCalls
   * @see AddVoicemail
   * @see UseSIP
   */
  object Phone : Permission(Call, ReadState, ReadNumber, AnswerCalls, AddVoicemail, UseSIP) {

    /**
     * Allows an application to initiate a phone call without going through the Dialer user
     * interface for the user to confirm the call.
     *
     * For `AndroidManifest.xml`: `android.permission.CALL_PHONE`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#CALL_PHONE)
     */
    object Call : Single(CALL_PHONE)

    /**
     * Allows read only access to phone state, including the current cellular network information,
     * the status of any ongoing calls, and a list of any PhoneAccounts registered on the device.
     *
     * Note: If both your minSdkVersion and targetSdkVersion values are set to 3 or lower, the
     * system implicitly grants your app this permission. If you don't need this permission, be
     * sure your targetSdkVersion is 4 or higher.
     *
     * For `AndroidManifest.xml`: `android.permission.READ_PHONE_STATE`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_PHONE_STATE)
     */
    object ReadState : Single(READ_PHONE_STATE)

    /**
     * Allows read access to the device's phone number(s). This is a subset of the capabilities
     * granted by READ_PHONE_STATE but is exposed to instant applications.
     *
     * For `AndroidManifest.xml`: `android.permission.READ_PHONE_NUMBERS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_PHONE_NUMBERS)
     */
    @RequiresApi(O) object ReadNumber : Single(READ_PHONE_NUMBERS)

    /**
     * Allows the app to answer an incoming phone call.
     *
     * For `AndroidManifest.xml`: `android.permission.ANSWER_PHONE_CALLS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#ANSWER_PHONE_CALLS)
     */
    @RequiresApi(O) object AnswerCalls : Single(ANSWER_PHONE_CALLS)
  }

  /**
   * Allows an application to add voicemails into the system.
   *
   * For `AndroidManifest.xml`: `android.permission.ADD_VOICEMAIL`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#ADD_VOICEMAIL)
   */
  object AddVoicemail : Single(ADD_VOICEMAIL)

  /**
   * Allows an application to use SIP service.
   *
   * For `AndroidManifest.xml`: `android.permission.USE_SIP`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#USE_SIP)
   */
  object UseSIP : Single(USE_SIP)

  /**
   * Allows an application to see the number being dialed during an outgoing call with the option
   * to redirect the call to a different number or abort the call altogether.
   *
   * This is a hard restricted permission which cannot be held by an app until the installer on
   * record whitelists the permission. For for more details see
   * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
   *
   * For `AndroidManifest.xml`: `android.permission.PROCESS_OUTGOING_CALLS`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#PROCESS_OUTGOING_CALLS)
   */
  @Deprecated(
    message = "This constant was deprecated in API level 29. " +
      "Applications should use CallRedirectionService instead of the Intent.ACTION_NEW_OUTGOING_CALL broadcast.",
    replaceWith = ReplaceWith("CallRedirectionService", "android.telecom.CallRedirectionService")
  )
  object ProcessOutgoingCalls : Single(PROCESS_OUTGOING_CALLS)

  /**
   * Allows an application to access data from sensors that the user uses to measure what is
   * happening inside their body, such as heart rate.
   *
   * For `AndroidManifest.xml`: `android.permission.BODY_SENSORS`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#BODY_SENSORS)
   * [Refer](https://developer.android.com/reference/android/Manifest.permission_group#SENSORS)
   */
  object Sensors : Single(BODY_SENSORS)

  /**
   * Used for permissions that allow accessing the device location.
   *
   * @see SMS.Send
   * @see SMS.Read
   * @see SMS.Receive
   */
  object SMS : Permission(Send, Read, Receive, ReceiveMMS, ReceiveWapPush) {

    /**
     * Allows an application to send SMS messages.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.SEND_SMS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#SEND_SMS)
     */
    object Send : Single(SEND_SMS)

    /**
     * Allows an application to read SMS messages.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.READ_SMS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_SMS)
     */
    object Read : Single(READ_SMS)

    /**
     * Allows an application to receive SMS messages.
     *
     * This is a hard restricted permission which cannot be held by an app until the installer on
     * record whitelists the permission. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.RECEIVE_SMS`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#RECEIVE_SMS)
     */
    object Receive : Single(RECEIVE_SMS)
  }

  /**
   * Allows an application to monitor incoming MMS messages.
   *
   * This is a hard restricted permission which cannot be held by an app until the installer on
   * record whitelists the permission. For for more details see
   * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
   *
   * For `AndroidManifest.xml`: `android.permission.RECEIVE_MMS`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#RECEIVE_MMS)
   */
  object ReceiveMMS : Single(RECEIVE_MMS)

  /**
   * Allows an application to receive WAP push messages.
   *
   * This is a hard restricted permission which cannot be held by an app until the installer on
   * record whitelists the permission. For for more details see
   * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
   *
   * For `AndroidManifest.xml`: `android.permission.RECEIVE_WAP_PUSH`.
   *
   * [Refer](https://developer.android.com/reference/android/Manifest.permission#RECEIVE_WAP_PUSH)
   */
  object ReceiveWapPush : Single(RECEIVE_WAP_PUSH)

  /**
   * Used for runtime permissions related to the shared external storage.
   *
   * @see Storage.ReadExternal
   * @see Storage.WriteExternal
   */
  object Storage : Permission(ReadExternal, WriteExternal) {

    /**
     *
     * Allows an application to read from external storage.
     *
     * Any app that declares the [WriteExternal] permission is implicitly granted this permission.
     *
     * This permission is enforced starting in API level 19. Before API level 19, this permission
     * is not enforced and all apps still have access to read from external storage. You can test
     * your app with the permission enforced by enabling Protect USB storage under Developer
     * options in the Settings app on a device running Android 4.1 or higher.
     *
     * Also starting in API level 19, this permission is not required to read/write files in your
     * application-specific directories returned by [Context.getExternalFilesDir]
     * and [Context.getExternalCacheDir].
     *
     * Note: If both your minSdkVersion and targetSdkVersion values are set to 3 or lower, the
     * system implicitly grants your app this permission. If you don't need this permission, be
     * sure your targetSdkVersion is 4 or higher.
     *
     * This is a soft restricted permission which cannot be held by an app it its full form until
     * the installer on record whitelists the permission. Specifically, if the permission is
     * allowlisted the holder app can access external storage and the visual and aural media
     * collections while if the permission is not allowlisted the holder app can only access to
     * the visual and aural medial collections. Also the permission is immutably restricted
     * meaning that the allowlist state can be specified only at install time and cannot change
     * until the app is installed. For for more details see
     * [PackageInstaller.SessionParams.setWhitelistedRestrictedPermissions].
     *
     * For `AndroidManifest.xml`: `android.permission.READ_EXTERNAL_STORAGE`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE)
     */
    object ReadExternal : Single(READ_EXTERNAL_STORAGE)

    /**
     * Allows an application to write to external storage.
     *
     * Note: If both your minSdkVersion and targetSdkVersion values are set to 3 or lower, the
     * system implicitly grants your app this permission. If you don't need this permission, be
     * sure your targetSdkVersion is 4 or higher.
     *
     * Starting in API level 19, this permission is not required to read/write files in your
     * application-specific directories returned by [Context.getExternalFilesDir]
     * and [Context.getExternalCacheDir].
     *
     * If this permission is not allowlisted for an app that targets an API level before
     * [Build.VERSION_CODES.Q] this permission cannot be granted to apps.
     *
     * For `AndroidManifest.xml`: `android.permission.WRITE_EXTERNAL_STORAGE`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#WRITE_EXTERNAL_STORAGE)
     */
    object WriteExternal : Single(WRITE_EXTERNAL_STORAGE)

    /**
     * Allows an application a broad access to external storage in scoped storage. Intended to be
     * used by few apps that need to manage files on behalf of the users.
     *
     * For `AndroidManifest.xml`: `android.permission.MANAGE_EXTERNAL_STORAGE`.
     *
     * [Refer](https://developer.android.com/reference/android/Manifest.permission#MANAGE_EXTERNAL_STORAGE)
     */
    @RequiresApi(VERSION_CODES.R) object ManageExternal : Single(MANAGE_EXTERNAL_STORAGE)
  }

  /**
   * Used for full runtime permissions related to the shared external storage.
   *
   * @see Storage.ReadExternal
   * @see Storage.WriteExternal
   * @see Storage.ManageExternal
   */
  @RequiresApi(VERSION_CODES.R) object FullStorage : Permission(
    Storage.ReadExternal,
    Storage.WriteExternal,
    Storage.ManageExternal
  )

  /**
   * Callback representing the result of permission request.
   *
   * The callback [invoke] will contain the granted or denied permissions, depending on different
   * request parameters.
   *
   * @author 凛 (https://github.com/RinOrz)
   */
  fun interface Callback {
    operator fun invoke(permissions: Set<String>)
  }
}

/**
 * Returns or create a permission instance according to the given [permission] information.
 *
 * @see Manifest.permission
 * @author 凛 (https://github.com/RinOrz)
 */
fun Permission(permission: String): Permission.Single = when (permission) {
  ACTIVITY_RECOGNITION -> ActivityRecognition
  READ_CALENDAR -> Calendar.Read
  WRITE_CALENDAR -> Calendar.Write
  READ_CALL_LOG -> CallLog.Read
  WRITE_CALL_LOG -> CallLog.Write
  CAMERA -> Camera
  READ_CONTACTS -> Contacts.Read
  WRITE_CONTACTS -> Contacts.Write
  GET_ACCOUNTS -> GetAccounts
  ACCESS_COARSE_LOCATION -> Location.AccessCoarse
  ACCESS_FINE_LOCATION -> Location.AccessFine
  ACCESS_BACKGROUND_LOCATION -> Location.AccessBackground
  ACCESS_MEDIA_LOCATION -> Location.AccessMedia
  RECORD_AUDIO -> Microphone
  "android.permission.BLUETOOTH_CONNECT" -> NearbyDevices.BluetoothConnect
  "android.permission.BLUETOOTH_SCAN" -> NearbyDevices.BluetoothScan
  CALL_PHONE -> Phone.Call
  READ_PHONE_STATE -> Phone.ReadState
  READ_PHONE_NUMBERS -> Phone.ReadNumber
  ANSWER_PHONE_CALLS -> Phone.AnswerCalls
  ADD_VOICEMAIL -> AddVoicemail
  USE_SIP -> UseSIP
  "android.permission.PROCESS_OUTGOING_CALLS" -> ProcessOutgoingCalls
  BODY_SENSORS -> Sensors
  SEND_SMS -> SMS.Send
  READ_SMS -> SMS.Read
  RECEIVE_SMS -> SMS.Receive
  RECEIVE_MMS -> ReceiveMMS
  RECEIVE_WAP_PUSH -> ReceiveWapPush
  MANAGE_EXTERNAL_STORAGE -> Storage.ManageExternal
  READ_EXTERNAL_STORAGE -> Storage.ReadExternal
  WRITE_EXTERNAL_STORAGE -> Storage.WriteExternal
  else -> Permission.Single(permission)
}