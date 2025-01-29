package com.aidannemeth.weatherly.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
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
class ObserveWeatherTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var server: MockWebServer

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var observeWeather: ObserveWeather

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
    fun given_no_cache_when_observe_weather_called_then_return_network() =
        runTest {
            val expected = weather.right()
            val mockResponse = MockResponse().setBody(json)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_cache_when_observe_weather_called_then_return_cache_and_refresh() =
        runTest {
            val cache = weather.right()
            val network = weather.right()
            val mockResponse = MockResponse().setBody(json)
            server.enqueue(mockResponse)
            weatherLocalDataSource.insertWeather(weather)

            observeWeather().test {
                assertEquals(cache, awaitItem())
                assertEquals(network, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_slow_network_when_observe_weather_called_then_return_network() =
        runTest {
            val expected = weather.right()
            val mockResponse = MockResponse().apply {
                setBody(json)
                throttleBody(1024, 1, TimeUnit.SECONDS)
            }
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_400_when_observe_weather_called_then_return_bad_request() =
        runTest {
            val expected = DataError.Remote.Http(
                networkError = NetworkError.BadRequest,
                apiErrorInfo = "HTTP 400 Client Error",
            ).left()
            val mockResponse = MockResponse().setResponseCode(400)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_401_when_observe_weather_called_then_return_unauthorized() =
        runTest {
            val expected = DataError.Remote.Http(
                networkError = NetworkError.Unauthorized,
                apiErrorInfo = "HTTP 401 Client Error",
            ).left()
            val mockResponse = MockResponse().setResponseCode(401)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_403_when_observe_weather_called_then_return_forbidden() =
        runTest {
            val expected = DataError.Remote.Http(
                networkError = NetworkError.Forbidden,
                apiErrorInfo = "HTTP 403 Client Error",
            ).left()
            val mockResponse = MockResponse().setResponseCode(403)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_404_when_observe_weather_called_then_return_not_found() =
        runTest {
            val expected = DataError.Remote.Http(
                networkError = NetworkError.NotFound,
                apiErrorInfo = "HTTP 404 Client Error",
            ).left()
            val mockResponse = MockResponse().setResponseCode(404)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_500_when_observe_weather_called_then_return_server_error() = runTest {
        val expected = DataError.Remote.Http(
            networkError = NetworkError.ServerError,
            apiErrorInfo = "HTTP 500 Server Error",
        ).left()
        val mockResponse = MockResponse().setResponseCode(500)
        server.enqueue(mockResponse)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            cancel()
        }
    }

    @Test
    fun given_unmapped_error_when_observe_weather_called_then_return_unknown() =
        runTest {
            val expected = DataError.Remote.Http(
                networkError = NetworkError.Unknown,
                apiErrorInfo = "HTTP 418 Client Error",
            ).left()
            val mockResponse = MockResponse().setResponseCode(418)
            server.enqueue(mockResponse)

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_disconnected_when_observe_weather_called_then_return_no_network() =
        runTest {
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

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }

    @Test
    fun given_malformed_json_when_observe_weather_called_then_return_parse() =
        runTest {
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

            observeWeather().test {
                assertEquals(expected, awaitItem())
                cancel()
            }
        }
}
