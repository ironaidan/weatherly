package com.aidannemeth.weatherly.core.network.retrofit

import com.aidannemeth.weatherly.core.network.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Weather
}
