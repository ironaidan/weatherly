package com.aidannemeth.weatherly.feature.weather.data.remote

import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Latitude,
        @Query("lon") longitude: Longitude,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): WeatherResponse
}
