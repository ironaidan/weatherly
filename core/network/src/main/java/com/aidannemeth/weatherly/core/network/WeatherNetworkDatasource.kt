package com.aidannemeth.weatherly.core.network

import com.aidannemeth.weatherly.core.network.model.NetworkWeather

interface WeatherNetworkDatasource {

    suspend fun getWeather(): NetworkWeather
}
