package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.RefreshWeather
import com.aidannemeth.weatherly.feature.weather.presentation.mapper.toWeatherUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherReducer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val observeWeather = mockk<ObserveWeather>(relaxed = true)

    private val reducer = mockk<WeatherReducer>(relaxed = true)

    private val refreshWeather = mockk<RefreshWeather>(relaxed = true)

    private val viewModel by lazy {
        WeatherViewModel(observeWeather, reducer, refreshWeather)
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        val expected = WeatherState(
            weatherMetadataState = WeatherMetadataState.Loading,
            isRefreshing = false,
        )

        viewModel.state.test {
            assertEquals(expected, awaitItem())
            verify { observeWeather() }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state is updated with weather data`() = runTest {
        val weather = WeatherSample.build()
        val weatherUiModel = weather.toWeatherUiModel()
        val expected = WeatherState(
            weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
            isRefreshing = false,
        )
        every { observeWeather() } returns flowOf(weather.right())
        every {
            reducer.getState(
                operation = WeatherEvent.WeatherData(weatherUiModel),
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = false,
                ),
            )
        } returns expected

        viewModel.state.test {
            ignoreInitialLoadingState()
            assertEquals(expected, awaitItem())
            verify {
                reducer.getState(
                    operation = WeatherEvent.WeatherData(weatherUiModel),
                    currentState = WeatherState(
                        weatherMetadataState = WeatherMetadataState.Loading,
                        isRefreshing = false,
                    ),
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state is updated with error`() = runTest {
        val expected = WeatherState(
            weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
            isRefreshing = false,
        )
        val error = DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = "apiErrorInfo",
        )
        every { observeWeather() } returns flowOf(error.left())
        every {
            reducer.getState(
                operation = WeatherEvent.ErrorLoadingWeather,
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = false,
                ),
            )
        } returns expected

        viewModel.state.test {
            ignoreInitialLoadingState()
            assertEquals(expected, awaitItem())
            verify {
                reducer.getState(
                    operation = WeatherEvent.ErrorLoadingWeather,
                    currentState = WeatherState(
                        weatherMetadataState = WeatherMetadataState.Loading,
                        isRefreshing = false,
                    ),
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun TurbineTestContext<WeatherState>.ignoreInitialLoadingState() {
        skipItems(1)
    }
}
