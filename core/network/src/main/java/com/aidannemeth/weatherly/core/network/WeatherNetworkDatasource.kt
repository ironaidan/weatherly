package com.aidannemeth.weatherly.core.network

import com.aidannemeth.weatherly.core.network.model.Weather

interface WeatherNetworkDatasource {

    suspend fun getWeather(): Weather
}
