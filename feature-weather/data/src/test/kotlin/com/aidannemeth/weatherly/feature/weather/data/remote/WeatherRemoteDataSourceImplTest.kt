package com.aidannemeth.weatherly.feature.weather.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Remote.Http
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.BadRequest
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.Forbidden
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.NoNetwork
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.NotFound
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.ServerError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.Unauthorized
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.Unknown
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError.UnprocessableEntity
import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

class WeatherRemoteDataSourceImplTest {

    private val weatherApi = mockk<WeatherApi>()

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private val weatherResponse = WeatherResponseSample.build()

    private val apiErrorInfo = "api error info"

    private lateinit var expected: Either<DataError.Remote, Weather>

    private lateinit var actual: Either<DataError.Remote, Weather>

    @Before
    fun setup() {
        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherApi)
    }

    @Test
    fun `get weather returns weather from network when successful`() = runTest {
        expected = toWeather(weatherResponse).right()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns weatherResponse.right()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns bad request when api returns 400`() = runTest {
        expected = Http(BadRequest, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(400, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns bad request when api returns 401`() = runTest {
        expected = Http(Unauthorized, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(401, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns forbidden when api returns 403`() = runTest {
        expected = Http(Forbidden, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(403, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns not found when api returns 404`() = runTest {
        expected = Http(NotFound, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(404, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns unprocessable entity when api returns 422`() = runTest {
        expected = Http(UnprocessableEntity, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(422, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns server error when api returns 500`() = runTest {
        expected = Http(ServerError, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(500, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns unknown when api returns unhandled error code`() = runTest {
        expected = Http(Unknown, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns HttpError(418, apiErrorInfo, "").left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns no network when api returns io error`() = runTest {
        expected = Http(NoNetwork, apiErrorInfo, true).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns IOError(IOException(apiErrorInfo)).left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns unknown when api returns unexpected call`() = runTest {
        expected = Http(Unknown, apiErrorInfo, false).left()
        coEvery {
            weatherApi.getWeather(any(), any(), any(), any())
        } returns UnexpectedCallError(IOException(apiErrorInfo)).left()

        actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }
}
