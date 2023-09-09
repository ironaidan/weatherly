package com.aidannemeth.weatherly.core.network.retrofit

import com.aidannemeth.weatherly.core.common.network.Dispatcher
import com.aidannemeth.weatherly.core.common.network.WeatherlyDispatchers.IO
import com.aidannemeth.weatherly.core.network.BuildConfig
import com.aidannemeth.weatherly.core.network.WeatherNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitWeatherNetworkDatasource @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val service: WeatherService,
) : WeatherNetworkDatasource {

    override suspend fun getWeather() = withContext(dispatcher) {
        service.getWeather(
            latitude = "34.189857",
            longitude = "-118.451357",
            apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
            units = "imperial",
        )
    }
}
