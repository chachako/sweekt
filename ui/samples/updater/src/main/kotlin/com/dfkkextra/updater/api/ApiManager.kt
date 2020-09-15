package com.dfkkextra.updater.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/*
 * author: 凛
 * date: 2020/9/15 上午9:26
 * github: https://github.com/oh-Rin
 */
object ApiManager {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://2.gitlib.top:1699/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val Api: ApiService = retrofit.create(ApiService::class.java)
}