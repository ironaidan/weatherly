package com.aidannemeth.weatherly.feature.weather.data.remote

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Either<CallError, WeatherResponse>
}
