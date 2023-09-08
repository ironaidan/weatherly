package com.aidannemeth.weatherly.core.common.network

import javax.inject.Qualifier

@Suppress("unused")
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatchers: WeatherlyDispatchers)

enum class WeatherlyDispatchers {
    Main,
    Default,
    IO,
}
