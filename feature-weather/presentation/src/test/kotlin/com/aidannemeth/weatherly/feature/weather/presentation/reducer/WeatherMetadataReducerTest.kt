package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.sample.WeatherUiModelSample
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class WeatherMetadataReducerTest(
    private val testInput: TestInput
) {
    private val weatherReducer = WeatherMetadataReducer()

    @Test
    fun `should produce the expected new state`() = runTest {
        val actualState = weatherReducer.dispatch(testInput.operation, testInput.currentState)

        assertEquals(testInput.expectedState, actualState)
    }

    companion object {

        private val weatherUiModel = WeatherUiModelSample.build()
        private val updatedWeatherUiModel = weatherUiModel.copy(temperature = "101")

        private val transitionsFromLoadingState = listOf(
            TestInput(
                currentState = WeatherMetadataState.Loading,
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherMetadataState.Data(weatherUiModel),
            ),
            TestInput(
                currentState = WeatherMetadataState.Loading,
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Error("Error loading weather"),
            ),
        )

        private val transitionsFromErrorState = listOf(
            TestInput(
                currentState = WeatherMetadataState.Error("Error loading weather"),
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherMetadataState.Data(weatherUiModel),
            ),
            TestInput(
                currentState = WeatherMetadataState.Error("Error loading weather"),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Error("Error loading weather")
            ),
        )

        private val transitionsFromDataState = listOf(
            TestInput(
                currentState = WeatherMetadataState.Data(weatherUiModel),
                operation = WeatherEvent.WeatherData(updatedWeatherUiModel),
                expectedState = WeatherMetadataState.Data(updatedWeatherUiModel),
            ),
            TestInput(
                currentState = WeatherMetadataState.Data(weatherUiModel),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Data(weatherUiModel)
            ),
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<TestInput> =
            transitionsFromLoadingState + transitionsFromDataState + transitionsFromErrorState
    }

    class TestInput(
        val currentState: WeatherMetadataState,
        val operation: WeatherOperation,
        val expectedState: WeatherMetadataState
    )
}
