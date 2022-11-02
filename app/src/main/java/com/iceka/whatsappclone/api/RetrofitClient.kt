package com.iceka.whatsappclone.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

   private const val mBaseUrl = "http://10.200.207.229:7170/"

   private val gson = GsonBuilder().setLenient().create()
   private val retrofit = Retrofit.Builder()
    .baseUrl(mBaseUrl)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .client(
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
             .setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS).build()
    ).build()


  object ApiClient
   {
    @JvmStatic
    fun getClient(): Retrofit = retrofit
   }
