package com.aidannemeth.weatherly.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Buffer
import org.junit.Rule
import java.util.concurrent.TimeUnit
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
        val expected = weather.right()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsCacheThenRefreshes() = runTest {
        val firstExpected = weather.right()
        val secondExpected = weather.right()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        weatherLocalDataSource.insertWeather(weather)

        weatherRepository.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsNetworkWhenNoCacheWithSlowNetwork() = runTest {
        val expected = weather.right()
        val mockResponse = MockResponse().apply {
            setBody(json)
            throttleBody(1024, 1, TimeUnit.SECONDS);
        }
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsBadRequestWhenServerReturns400() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.BadRequest,
            apiErrorInfo = "HTTP 400 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(400)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsUnauthorizedWhenServerReturns401() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Unauthorized,
            apiErrorInfo = "HTTP 401 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(401)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsForbiddenWhenServerReturns403() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Forbidden,
            apiErrorInfo = "HTTP 403 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(403)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsNotFoundWhenServerReturns404() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.NotFound,
            apiErrorInfo = "HTTP 404 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(404)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsServerErrorWhenServerReturns500() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.ServerError,
            apiErrorInfo = "HTTP 500 Server Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(500)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsUnknownWhenServerReturnsUnmappedError() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Unknown,
            apiErrorInfo = "HTTP 418 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(418)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsNoNetworkWhenRequestIsDisconnected() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = "unexpected end of stream",
        ).left()
        val byteArrayStub = ByteArray(1024)
        val mockResponse = MockResponse().apply {
            setBody(Buffer().write(byteArrayStub))
            setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
        }
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun observeWeatherReturnsParseWhenRequestIsMalformed() = runTest {
        val resourcePath = "assets/mock_malformed_get_weather_response.json"
        val json = this::class.java.classLoader
            ?.getResourceAsStream(resourcePath)
            .let { requireNotNull(it) { "Could not find resource file: $resourcePath" } }
            .bufferedReader()
            .use { it.readText() }
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Parse,
            apiErrorInfo = "Field 'lat' is required for type with serial name 'com.aidannemeth." +
                    "weatherly.feature.weather.data.remote.response.WeatherResponse', but it was" +
                    " missing at path: \$",
        ).left()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun refreshWeatherReturnsWeather() = runTest {
        val expected = weather.right()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsWeatherWithSlowNetwork() = runTest {
        val expected = weather.right()
        val mockResponse = MockResponse().apply {
            setBody(json)
            throttleBody(1024, 1, TimeUnit.SECONDS);
        }
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsBadRequestWhenServerReturns400() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.BadRequest,
            apiErrorInfo = "HTTP 400 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(400)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()
        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsUnauthorizedWhenServerReturns401() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Unauthorized,
            apiErrorInfo = "HTTP 401 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(401)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsForbiddenWhenServerReturns403() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Forbidden,
            apiErrorInfo = "HTTP 403 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(403)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsNotFoundWhenServerReturns404() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.NotFound,
            apiErrorInfo = "HTTP 404 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(404)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsServerErrorWhenServerReturns500() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.ServerError,
            apiErrorInfo = "HTTP 500 Server Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(500)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsUnknownWhenServerReturnsUnmappedError() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Unknown,
            apiErrorInfo = "HTTP 418 Client Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(418)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsNoNetworkWhenRequestIsDisconnected() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = "unexpected end of stream",
        ).left()
        val byteArrayStub = ByteArray(1024)
        val mockResponse = MockResponse().apply {
            setBody(Buffer().write(byteArrayStub))
            setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
        }
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun refreshWeatherReturnsParseWhenRequestIsMalformed() = runTest {
        val resourcePath = "assets/mock_malformed_get_weather_response.json"
        val json = this::class.java.classLoader
            ?.getResourceAsStream(resourcePath)
            .let { requireNotNull(it) { "Could not find resource file: $resourcePath" } }
            .bufferedReader()
            .use { it.readText() }
        val expected = DataError.Remote.Http(
            networkError = NetworkError.Parse,
            apiErrorInfo = "Field 'lat' is required for type with serial name 'com.aidannemeth." +
                    "weatherly.feature.weather.data.remote.response.WeatherResponse', but it was" +
                    " missing at path: \$",
        ).left()
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        val actual = weatherRepository.refreshWeather()

        assertEquals(expected, actual)
    }
}
