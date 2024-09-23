package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.presentation.mapper.toWeatherUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherMetadataReducer
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

    private val reducer = mockk<WeatherMetadataReducer>(relaxed = true)

    private val viewModel by lazy {
        WeatherViewModel(observeWeather, reducer)
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
        val expected = WeatherMetadataState.Loading

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
        val expected = WeatherMetadataState.Data(weatherUiModel)
        every { observeWeather() } returns flowOf(weather.right())
        every {
            reducer.dispatch(
                operation = WeatherEvent.WeatherData(weatherUiModel),
                currentState = WeatherMetadataState.Loading,
            )
        } returns WeatherMetadataState.Data(weatherUiModel)

        viewModel.state.test {
            ignoreInitialLoadingState()
            assertEquals(expected, awaitItem())
            verify {
                reducer.dispatch(
                    operation = WeatherEvent.WeatherData(weatherUiModel),
                    currentState = WeatherMetadataState.Loading,
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state is updated with error`() = runTest {
        val expected = WeatherMetadataState.Error("Error loading weather")
        val error = DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = "apiErrorInfo",
        )
        every { observeWeather() } returns flowOf(error.left())
        every {
            reducer.dispatch(
                operation = WeatherEvent.ErrorLoadingWeather,
                currentState = WeatherMetadataState.Loading,
            )
        } returns WeatherMetadataState.Error("Error loading weather")

        viewModel.state.test {
            ignoreInitialLoadingState()
            assertEquals(expected, awaitItem())
            verify {
                reducer.dispatch(
                    operation = WeatherEvent.ErrorLoadingWeather,
                    currentState = WeatherMetadataState.Loading,
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun TurbineTestContext<WeatherMetadataState>.ignoreInitialLoadingState() {
        skipItems(1)
    }
}
