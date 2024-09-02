package com.aidannemeth.weatherly.network

import arrow.core.right
import com.aidannemeth.weatherly.feature.weather.data.remote.WeatherApi
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Buffer
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val BASE_URL = "/test/"

class WeatherApiTest {
    private lateinit var server: MockWebServer

    private val weatherApi by lazy {
        val baseUrl = server.url(BASE_URL)
        val retrofit = buildRetrofit(baseUrl.toString(), Json { ignoreUnknownKeys = true })
        retrofit.create(WeatherApi::class.java)
    }

    private val resourcePath = "assets/mock_get_weather_response.json"

    private val json by lazy {
        this::class.java.classLoader
            ?.getResourceAsStream(resourcePath)
            .let { requireNotNull(it) { "Could not find resource file: $resourcePath" } }
            .bufferedReader()
            .use { it.readText() }
    }

    private val expected = WeatherResponseSample.build().right()

    @BeforeTest
    fun setup() {
        server = MockWebServer()
    }

    @AfterTest
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `get weather uses correct method and path when invoked`() = runTest {
        val latitude = 34.149956
        val longitude = -118.448891
        val apiKey = "apiKey"
        val units = "imperial"
        val expectedPath =
            "${BASE_URL}data/3.0/onecall" +
                    "?lat=$latitude" +
                    "&lon=$longitude" +
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
    fun `get weather returns weather response when request is successful`() = runTest {
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)

        val actual = weatherApi.getWeather(0.0, 0.0, "", "")

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns weather response with slow network`() = runTest {
        val mockResponse = MockResponse().apply {
            setBody(json)
            throttleBody(1024, 1, TimeUnit.SECONDS);
        }
        server.enqueue(mockResponse)

        val actual = weatherApi.getWeather(0.0, 0.0, "", "")

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns call error when server returns 400`() = runTest {
        val mockResponse = MockResponse().setResponseCode(400)
        server.enqueue(mockResponse)

        val response = weatherApi.getWeather(0.0, 0.0, "", "")

        assertTrue(response.isLeft())
    }

    @Test
    fun `get weather returns call error when server returns 500`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        server.enqueue(mockResponse)

        val response = weatherApi.getWeather(0.0, 0.0, "", "")

        assertTrue(response.isLeft())
    }

    @Test
    fun `get weather returns call error when request is disconnected`() = runTest {
        val byteArrayStub = ByteArray(1024)
        val mockResponse = MockResponse().apply {
            setBody(Buffer().write(byteArrayStub))
            setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
        }
        server.enqueue(mockResponse)

        val actual = weatherApi.getWeather(0.0, 0.0, "", "")

        assertTrue(actual.isLeft())
    }
}
