package com.aquiles.hotel.libs.newsapilib.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    private var mRetrofit: Retrofit? = null
    private val retrofit: Retrofit?
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
    @JvmStatic
    val aPIService: APIService
        get() = retrofit!!.create(APIService::class.java)
}