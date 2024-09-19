package com.aidannemeth.weatherly.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@HiltAndroidTest
class WeatherRepositoryImplTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var server: MockWebServer

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var weatherLocalDataSource: WeatherLocalDataSource

    private val resourcePath = "assets/mock_get_weather_response.json"

    private val json by lazy {
        this::class.java.classLoader
            ?.getResourceAsStream(resourcePath)
            .let { requireNotNull(it) { "Could not find resource file: $resourcePath" } }
            .bufferedReader()
            .use { it.readText() }
    }

    private val weather = WeatherSample.build()

    @BeforeTest
    fun setup() {
        hiltRule.inject()
    }

    @AfterTest
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun observeWeatherReturnsNetworkWhenNoCache() = runTest {
        val firstExpected = DataError.Remote.Loading.left()
        val secondExpected = weather.right()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsCacheThenRefreshes() = runTest {
        val firstExpected = weather.right()
        val secondExpected = DataError.Remote.Loading.left()
        val thirdExpected = weather.right()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        weatherLocalDataSource.insertWeather(weather)

        weatherRepository.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            assertEquals(thirdExpected, awaitItem())
            cancel()
        }
    }
}
