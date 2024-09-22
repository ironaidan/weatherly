package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherMetadataReducer
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val observeWeather = mockk<ObserveWeather>()

    private val reducer = mockk<WeatherMetadataReducer>()

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

//    @Test
//    fun `initial state is weatherly`() = runTest {
//        val expected = WeatherMetadataState.Loading
//
//        viewModel.state.test {
//            assertEquals(expected, awaitItem())
//        }
//    }
}
