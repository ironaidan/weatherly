package com.aidannemeth.weatherly

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.aidannemeth.weatherly.navigation.Home
import com.aidannemeth.weatherly.ui.theme.WeatherlyTheme
import org.junit.Rule
import org.junit.Test

class WeatherlyAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `title is displayed when app is launched`() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            WeatherlyTheme {
                Home()
            }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.app_name))
            .assertIsDisplayed()
    }
}
