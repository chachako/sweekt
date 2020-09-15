package com.dfkkextra.updater.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class BackupParams(
    @Json(name = "a16")
    val a16: String,
    @Json(name = "code")
    val code: Int,
    @Json(name = "codeMsg")
    val codeMsg: String,
    @Json(name = "device")
    val device: String,
    @Json(name = "prop")
    val prop: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "time")
    val time: Long,
    @Json(name = "wxid")
    val wxid: String
)