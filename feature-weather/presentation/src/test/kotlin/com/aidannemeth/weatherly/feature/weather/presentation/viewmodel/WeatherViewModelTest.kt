package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val viewModel by lazy {
        WeatherViewModel()
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
    fun `initial state is weatherly`() = runTest {
        val expected = "Weatherly"

        viewModel.state.test {
            assertEquals(expected, awaitItem())
        }
    }
}
