package com.dfkkextra.updater.api

import com.dfkkextra.updater.data.BackupParams
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
  @GET("cmd") suspend fun call(@Query("fun") method: String): BackupParams

  @GET("cmd") suspend fun call(@QueryMap params: Map<String, String>): BackupParams
}