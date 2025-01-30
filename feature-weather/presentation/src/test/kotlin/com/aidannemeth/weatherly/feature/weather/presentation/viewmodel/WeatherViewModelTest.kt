package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import arrow.core.left
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherRepositoryTestFake
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.RefreshWeather
import com.aidannemeth.weatherly.feature.weather.presentation.R
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherReducer
import com.aidannemeth.weatherly.feature.weather.presentation.sample.WeatherUiModelSample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val weatherRepository = WeatherRepositoryTestFake()

    private val observeWeather = ObserveWeather(
        weatherRepository = weatherRepository,
    )

    private val reducer = WeatherReducer()

    private val refreshWeather = RefreshWeather(
        weatherRepository = weatherRepository,
    )

    private val viewModel by lazy {
        WeatherViewModel(observeWeather, reducer, refreshWeather)
    }

    private val weatherUiModel = WeatherUiModelSample.build()

    private val refreshedWeatherUiModel = WeatherUiModelSample.buildIncreasedTemperature()

    private val error = DataError.Remote.Http(
        networkError = NetworkError.NoNetwork,
        apiErrorInfo = "apiErrorInfo",
    )

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
        val expected = WeatherState.Loading

        viewModel.state.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `state is updated with weather data`() = runTest {
        val expected = WeatherState.Loading.copy(
            weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
        )

        viewModel.state.test {
            skipInitialLoadingEvent()
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `state is updated with error`() = runTest {
        weatherRepository.observeWeatherResult = error.left()
        val expected = WeatherState.Loading.copy(
            weatherMetadataState = WeatherMetadataState.Error(
                TextUiModel(R.string.loading_error_message)
            ),
        )

        viewModel.state.test {
            skipInitialLoadingEvent()
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `state is updated with weather data when refreshed`() = runTest {
        val initialData = WeatherState.Loading.copy(
            weatherMetadataState = WeatherMetadataState.Data(
                weatherUiModel,
            ),
        )
        val refreshedData = initialData.copy(
            weatherMetadataState = WeatherMetadataState.Data(
                refreshedWeatherUiModel,
            ),
        )

        viewModel.state.test {
            skipInitialLoadingEvent()
            assertEquals(initialData, awaitItem())
            viewModel.dispatchAction(WeatherAction.RefreshWeather)
            assertEquals(refreshedData, awaitItem())
        }
    }

    @Test
    fun `state is updated with weather data when refresh fails`() = runTest {
        weatherRepository.refreshWeatherResult = error.left()
        val expected = WeatherState.Loading.copy(
            weatherMetadataState = WeatherMetadataState.Data(
                weatherUiModel,
            ),
        )

        viewModel.state.test {
            skipInitialLoadingEvent()
            assertEquals(expected, awaitItem())
            viewModel.dispatchAction(WeatherAction.RefreshWeather)
        }
    }

    private suspend fun TurbineTestContext<WeatherState>.skipInitialLoadingEvent() {
        skipItems(1)
    }
}
