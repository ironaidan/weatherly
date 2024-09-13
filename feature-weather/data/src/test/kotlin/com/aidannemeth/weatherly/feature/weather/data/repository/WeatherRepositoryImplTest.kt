package com.aidannemeth.weatherly.feature.weather.data.repository

import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import io.mockk.mockk
import org.junit.Before

class WeatherRepositoryImplTest {
    private val weatherLocalDataSource = mockk<WeatherLocalDataSource>()

    private val weatherRemoteDataSource = mockk<WeatherRemoteDataSource>()

    private lateinit var weatherRepository: WeatherRepositoryImpl

    private val weather = WeatherSample.build()

    @Before
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(
            weatherLocalDataSource,
            weatherRemoteDataSource,
        )
    }
}
