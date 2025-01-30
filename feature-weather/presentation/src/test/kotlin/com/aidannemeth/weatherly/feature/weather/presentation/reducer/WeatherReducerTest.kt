package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.R
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
        private val updatedWeatherUiModel = weatherUiModel.copy(temperature = TextUiModel("101"))

        private val transitionsFromLoadingState = listOf(
            TestInput(
                currentState = WeatherState.Loading,
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                ),
            ),
            TestInput(
                currentState = WeatherState.Loading,
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message)
                    ),
                ),
            ),
            TestInput(
                currentState = WeatherState.Loading,
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState.Loading,
            ),
        )

        private val transitionsFromErrorState = listOf(
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message),
                    ),
                ),
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message),
                    ),
                ),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message),
                    ),
                )
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message),
                    ),
                ),
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Error(
                        message = TextUiModel(R.string.loading_error_message),
                    ),
                )
            ),
        )

        private val transitionsFromDataState = listOf(
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                ),
                operation = WeatherEvent.WeatherData(updatedWeatherUiModel),
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(updatedWeatherUiModel),
                ),
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                ),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                )
            ),
            TestInput(
                currentState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
                ),
                operation = WeatherAction.RefreshWeather,
                expectedState = WeatherState(
                    weatherMetadataState = WeatherMetadataState.Data(weatherUiModel),
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
