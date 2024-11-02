package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.RefreshWeather
import com.aidannemeth.weatherly.feature.weather.presentation.mapper.toWeatherUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherReducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        dispatcher = UnconfinedTestDispatcher(),
        weatherRepository = weatherRepository,
    )

    private val reducer = WeatherReducer()

    private val refreshWeather = RefreshWeather(
        dispatcher = UnconfinedTestDispatcher(),
        weatherRepository = weatherRepository,
    )

    private val viewModel by lazy {
        WeatherViewModel(observeWeather, reducer, refreshWeather)
    }

    private val weather = WeatherSample.build()

    private val weatherUiModel = weather.toWeatherUiModel()

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
        val firstExpected = WeatherState.Loading
        val secondExpected = firstExpected.copy(
            weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
        )

        viewModel.state.test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
        }
    }

    @Test
    fun `state is updated with error`() = runTest {
        weatherRepository.setObserveWeatherResult(error.left())
        val firstExpected = WeatherState.Loading
        val secondExpected = firstExpected.copy(
            weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
        )

        viewModel.state.test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
        }
    }

    @Test
    fun `state is updated with weather data when refreshed`() = runTest {
        val refreshedWeather = weather.copy(
            temperature = Temperature(293.55f)
        )
        weatherRepository.setRefreshWeatherResult(refreshedWeather.right())
        val firstExpected = WeatherState.Loading
        val secondExpected = firstExpected.copy(
            weatherMetadataState = WeatherMetadataState.Data(
                weatherUiModel,
            ),
        )
        val thirdExpected = secondExpected.copy(isRefreshing = true)
        val fourthExpected = thirdExpected.copy(
            isRefreshing = false,
            weatherMetadataState = WeatherMetadataState.Data(
                refreshedWeather.toWeatherUiModel(),
            ),
        )

        viewModel.state.test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            viewModel.dispatchAction(WeatherAction.RefreshWeather)
            assertEquals(thirdExpected, awaitItem())
            assertEquals(fourthExpected, awaitItem())
        }
    }

    @Test
    fun `state is updated with weather data when refresh fails`() = runTest {
        weatherRepository.setRefreshWeatherResult(error.left())
        val firstExpected = WeatherState.Loading
        val secondExpected = firstExpected.copy(
            weatherMetadataState = WeatherMetadataState.Data(
                weatherUiModel,
            ),
        )
        val thirdExpected = secondExpected.copy(isRefreshing = true)
        val fourthExpected = thirdExpected.copy(isRefreshing = false)

        viewModel.state.test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            viewModel.dispatchAction(WeatherAction.RefreshWeather)
            assertEquals(thirdExpected, awaitItem())
            assertEquals(fourthExpected, awaitItem())
        }
    }
}

class WeatherRepositoryTestFake : WeatherRepository {
    private var observeWeatherResult: Either<DataError, Weather> =
        WeatherSample.build().right()

    private var refreshWeatherResult: Either<DataError.Remote, Weather> =
        WeatherSample.build().right()

    fun setObserveWeatherResult(result: Either<DataError, Weather>) {
        observeWeatherResult = result
    }

    fun setRefreshWeatherResult(result: Either<DataError.Remote, Weather>) {
        refreshWeatherResult = result
    }

    override fun observeWeather(): Flow<Either<DataError, Weather>> {
        return flowOf(observeWeatherResult)
    }

    override suspend fun refreshWeather(): Either<DataError.Remote, Weather> {
        return refreshWeatherResult
    }
}
