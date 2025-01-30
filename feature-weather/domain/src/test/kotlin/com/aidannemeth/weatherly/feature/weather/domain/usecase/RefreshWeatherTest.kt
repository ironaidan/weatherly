package com.aidannemeth.weatherly.feature.weather.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(Parameterized::class)
class RefreshWeatherTest(
    private val testInput: Either<DataError.Remote, Weather>
) {
    private val weatherRepository = mockk<WeatherRepository>()

    private lateinit var refreshWeatherUseCase: RefreshWeather

    @BeforeTest
    fun setup() {
        refreshWeatherUseCase = RefreshWeather(weatherRepository)
    }

    @Test
    fun `refresh weather returns expected response`() = runTest {
        val expected = testInput
        coEvery { weatherRepository.refreshWeather() } returns expected

        val actual = refreshWeatherUseCase()

        assertEquals(expected, actual)
        coVerify { weatherRepository.refreshWeather() }
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Either<DataError.Remote, Weather>> = listOf(
            WeatherSample.build().right(),
            DataError.Remote.Http(
                networkError = NetworkError.Forbidden,
                apiErrorInfo = "HTTP 403 Client Error",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.NoNetwork,
                apiErrorInfo = "unexpected end of stream",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.NotFound,
                apiErrorInfo = "HTTP 404 Client Error",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.ServerError,
                apiErrorInfo = "HTTP 500 Server Error",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.Unauthorized,
                apiErrorInfo = "HTTP 401 Client Error",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.Parse,
                apiErrorInfo = "apiErrorInfo",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.BadRequest,
                apiErrorInfo = "HTTP 400 Client Error",
            ).left(),
            DataError.Remote.Http(
                networkError = NetworkError.Unknown,
                apiErrorInfo = "apiErrorInfo",
            ).left(),
            DataError.Remote.NoNewData.left(),
            DataError.Remote.Unknown.left(),
        )
    }
}
