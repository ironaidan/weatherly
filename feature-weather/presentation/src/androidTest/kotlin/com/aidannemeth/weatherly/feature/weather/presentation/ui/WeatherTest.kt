package com.aidannemeth.weatherly.feature.weather.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.common.presentation.theme.WeatherlyTheme
import com.aidannemeth.weatherly.feature.weather.presentation.R
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import org.junit.Rule
import org.junit.Test

class WeatherTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `loading indicator is displayed when state is loading`() {
        composeTestRule.setContent {
            WeatherlyTheme {
                WeatherScreen(
                    state = WeatherState(
                        weatherMetadataState = WeatherMetadataState.Loading,
                    ),
                    actions = Weather.Actions.Empty,
                )
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun `temperature is displayed when state is data`() {
        composeTestRule.setContent {
            WeatherlyTheme {
                WeatherScreen(
                    state = WeatherState(
                        weatherMetadataState = WeatherMetadataState.Data(
                            WeatherMetadataUiModel(
                                temperature = TextUiModel(value = "100"),
                            )
                        ),
                    ),
                    actions = Weather.Actions.Empty,
                )
            }
        }

        composeTestRule.onNodeWithText("100").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsNotDisplayed()
    }

    @Test
    fun `snackbar is displayed when state is error`() {
        composeTestRule.setContent {
            WeatherlyTheme {
                WeatherScreen(
                    state = WeatherState(
                        weatherMetadataState = WeatherMetadataState.Error(
                            message = TextUiModel(R.string.loading_error_message),
                        ),
                    ),
                    actions = Weather.Actions.Empty,
                )
            }
        }

        composeTestRule.onNodeWithText("Error loading weather").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsNotDisplayed()
    }
}
