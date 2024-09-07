package com.aidannemeth.weatherly.network

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.aidannemeth.weatherly.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber


fun buildRetrofit(baseUrl: String, json: Json): Retrofit {
    val logging = HttpLoggingInterceptor { message ->
        Timber.tag("OkHttp").d(message)
    }
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(logging)
        }
    }.build()
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(EitherCallAdapterFactory())
        .build()
}
