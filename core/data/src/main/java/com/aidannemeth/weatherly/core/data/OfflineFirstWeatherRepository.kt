package com.aidannemeth.weatherly.core.data

import com.aidannemeth.weatherly.core.common.network.Dispatcher
import com.aidannemeth.weatherly.core.common.network.WeatherlyDispatchers.IO
import com.aidannemeth.weatherly.core.data.model.asExternalModel
import com.aidannemeth.weatherly.core.network.WeatherNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Make this offline first when we have a database.
class OfflineFirstWeatherRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val network: WeatherNetworkDatasource,
) : WeatherRepository {

    override suspend fun getWeather() = withContext(dispatcher) {
        network.getWeather().asExternalModel()
    }
}
