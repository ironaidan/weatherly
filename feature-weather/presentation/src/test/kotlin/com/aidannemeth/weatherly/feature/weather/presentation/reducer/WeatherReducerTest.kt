package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.sample.WeatherUiModelSample
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class WeatherReducerTest(
    private val testInput: TestInput
) {
    private val weatherReducer = WeatherReducer()

    @Test
    fun `should produce the expected new state`() = runTest {
        val actualState = weatherReducer.getState(testInput.operation, testInput.currentState)

        assertEquals(testInput.expectedState, actualState)
    }

    companion object {

        private val weatherUiModel = WeatherUiModelSample.build()
        private val updatedWeatherUiModel = weatherUiModel.copy(temperature = "101")

        private val transitionsFromLoadingState = listOf(
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = false,
                ),
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = false,
                ),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = false,
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = false,
                ),
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Loading,
                    isRefreshing = true,
                ),
            ),
        )

        private val transitionsFromErrorState = listOf(
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = false,
                ),
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = false,
                    ),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = false,
                )
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = false,
                    ),
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error("Error loading weather"),
                    isRefreshing = true,
                )
            ),
        )

        private val transitionsFromDataState = listOf(
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                ),
                operation = WeatherEvent.WeatherData(updatedWeatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(updatedWeatherUiModel),
                    isRefreshing = false,
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                ),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                )
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = false,
                ),
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                    isRefreshing = true,
                )
            ),
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<TestInput> =
            transitionsFromLoadingState + transitionsFromDataState + transitionsFromErrorState
    }

    class TestInput(
        val currentState: WeatherState,
        val operation: WeatherOperation,
        val expectedState: WeatherState
    )
}
