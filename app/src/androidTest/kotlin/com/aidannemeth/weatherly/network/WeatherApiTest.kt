package com.aidannemeth.weatherly.network

import com.aidannemeth.weatherly.feature.weather.data.remote.WeatherApi
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Units
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Buffer
import okio.ProtocolException
import org.junit.Rule
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@HiltAndroidTest
class WeatherApiTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var server: MockWebServer

    @Inject
    lateinit var weatherApi: WeatherApi

    private val resourcePath = "assets/mock_get_weather_response.json"

    private val json by lazy {
        this::class.java.classLoader
            ?.getResourceAsStream(resourcePath)
            .let { requireNotNull(it) { "Could not find resource file: $resourcePath" } }
            .bufferedReader()
            .use { it.readText() }
    }

    private val expected = WeatherResponseSample.build()

    @BeforeTest
    fun init() {
        hiltRule.inject()
    }

    @AfterTest
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun when_get_weather_called_then_correct_method_and_path_used() =
        runTest {
            val mockResponse = MockResponse().setBody(json)
            server.enqueue(mockResponse)
            val latitude = Latitude(34.149956)
            val longitude = Longitude(-118.448891)
            val apiKey = "apiKey"
            val units = Units.Imperial.name.lowercase()
            val expectedPath =
                "/test/data/3.0/onecall" +
                        "?lat=${latitude.value}" +
                        "&lon=${longitude.value}" +
                        "&appid=$apiKey" +
                        "&units=$units"
            val expectedMethod = "GET"

            weatherApi.getWeather(latitude, longitude, apiKey, units)

            val request = server.takeRequest()
            val actualMethod = request.method
            val actualPath = request.path
            assertEquals(expectedMethod, actualMethod)
            assertEquals(expectedPath, actualPath)
        }

    @Test
    fun when_get_weather_called_then_return_weather() =
        runTest {
            val mockResponse = MockResponse().setBody(json)
            server.enqueue(mockResponse)

            val actual = weatherApi.getWeather(Latitude(0.0), Longitude(0.0), "", "")

            assertEquals(expected, actual)
        }

    @Test
    fun given_slow_network_when_get_weather_called_then_return_weather() =
        runTest {
            val mockResponse = MockResponse().apply {
                setBody(json)
                throttleBody(1024, 1, TimeUnit.SECONDS)
            }
            server.enqueue(mockResponse)

            val actual = weatherApi.getWeather(Latitude(0.0), Longitude(0.0), "", "")

            assertEquals(expected, actual)
        }

    @Test
    fun given_400_when_get_weather_called_then_return_http_exception() =
        runTest {
            val expected = 400
            val mockResponse = MockResponse().setResponseCode(expected)
            server.enqueue(mockResponse)

            val actual = assertFailsWith<HttpException> {
                weatherApi.getWeather(Latitude(0.0), Longitude(0.0), "", "")
            }.code()

            assertEquals(expected, actual)
        }

    @Test
    fun given_500_when_get_weather_called_then_return_http_exception() =
        runTest {
            val expected = 500
            val mockResponse = MockResponse().setResponseCode(expected)
            server.enqueue(mockResponse)

            val actual = assertFailsWith<HttpException> {
                weatherApi.getWeather(Latitude(0.0), Longitude(0.0), "", "")
            }.code()

            assertEquals(expected, actual)
        }

    @Test
    fun given_disconnection_when_get_weather_called_then_return_protocol_exception() =
        runTest {
            val byteArrayStub = ByteArray(1024)
            val mockResponse = MockResponse().apply {
                setBody(Buffer().write(byteArrayStub))
                setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
            }
            server.enqueue(mockResponse)

            assertFailsWith<ProtocolException> {
                weatherApi.getWeather(Latitude(0.0), Longitude(0.0), "", "")
            }
        }
}