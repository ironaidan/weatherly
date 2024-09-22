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
            ).toArray(),
            TestInput(
                currentState = WeatherMetadataState.Loading,
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Error("Error loading weather"),
            ).toArray(),
        )

        private val transitionsFromErrorState = listOf(
            TestInput(
                currentState = WeatherMetadataState.Error("Error loading weather"),
                operation = WeatherEvent.WeatherData(weatherUiModel),
                expectedState = WeatherMetadataState.Data(weatherUiModel),
            ).toArray(),
            TestInput(
                currentState = WeatherMetadataState.Error("Error loading weather"),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Error("Error loading weather")
            ).toArray(),
        )

        private val transitionsFromDataState = arrays()

        private fun arrays() = listOf(
            TestInput(
                currentState = WeatherMetadataState.Data(weatherUiModel),
                operation = WeatherEvent.WeatherData(updatedWeatherUiModel),
                expectedState = WeatherMetadataState.Data(updatedWeatherUiModel),
            ).toArray(),
            TestInput(
                currentState = WeatherMetadataState.Data(weatherUiModel),
                operation = WeatherEvent.ErrorLoadingWeather,
                expectedState = WeatherMetadataState.Data(weatherUiModel)
            ).toArray(),
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<TestInput>> =
            transitionsFromLoadingState + transitionsFromDataState + transitionsFromErrorState
    }

    class TestInput(
        val currentState: WeatherMetadataState,
        val operation: WeatherOperation,
        val expectedState: WeatherMetadataState
    ) {

        fun toArray() = arrayOf(this)
    }
}
