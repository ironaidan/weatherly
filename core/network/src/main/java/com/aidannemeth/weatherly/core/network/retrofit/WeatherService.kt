package com.aidannemeth.weatherly.core.network.retrofit

import com.aidannemeth.weatherly.core.network.model.Weather
import retrofit2.http.GET

interface WeatherService {

    @GET("users/{user}/repos")
    suspend fun getWeather(): Weather
}
