package com.aidannemeth.weatherly.di

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.aidannemeth.weatherly.BuildConfig
import com.aidannemeth.weatherly.feature.weather.data.remote.WeatherApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: HttpUrl,
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder().apply {
        val contentType = "application/json".toMediaType()
        baseUrl(baseUrl)
        client(okHttpClient)
        addConverterFactory(json.asConverterFactory(contentType))
        addCallAdapterFactory(EitherCallAdapterFactory())
    }.build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {
    @Provides
    fun provideBaseHttpUrl(): HttpUrl = "https://api.openweathermap.org/".toHttpUrl()
}
