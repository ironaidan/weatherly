package com.aidannemeth.weatherly

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.aidannemeth.weatherly.ui.theme.WeatherlyTheme
import org.junit.Rule
import org.junit.Test

class WeatherlyAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun titleIsDisplayed() {
        composeTestRule.setContent {
            WeatherlyTheme {
                WeatherlyApp()
            }
        }

        composeTestRule.onNodeWithText("Weatherly").assertIsDisplayed()
    }
}
