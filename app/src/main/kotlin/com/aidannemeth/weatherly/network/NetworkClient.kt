package com.aidannemeth.weatherly.network

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

fun buildRetrofit(baseUrl: String, json: Json): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(EitherCallAdapterFactory())
        .build()
}
