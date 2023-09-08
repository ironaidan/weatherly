package com.aidannemeth.weatherly.core.network.retrofit

import com.aidannemeth.weatherly.core.network.BuildConfig
import com.aidannemeth.weatherly.core.network.WeatherNetworkDatasource
import com.aidannemeth.weatherly.core.network.model.NetworkWeather
import javax.inject.Inject

class RetrofitWeatherNetworkDatasource @Inject constructor(
    private val service: WeatherService,
) : WeatherNetworkDatasource {

    override suspend fun getWeather(): NetworkWeather {
        return service.getWeather(
            latitude = "34.189857",
            longitude = "-118.451357",
            apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
            units = "imperial",
        )
    }
}
