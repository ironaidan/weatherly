package com.aidannemeth.weatherly.core.network.retrofit

import com.aidannemeth.weatherly.core.network.WeatherNetworkDatasource
import javax.inject.Inject

class RetrofitWeatherNetworkDatasource @Inject constructor(
    private val service: WeatherService,
) : WeatherNetworkDatasource {

    override suspend fun getWeather() = service.getWeather()
}
