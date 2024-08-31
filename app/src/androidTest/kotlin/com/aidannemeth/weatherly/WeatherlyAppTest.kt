package com.aidannemeth.weatherly

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class WeatherlyAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `title is displayed when app is launched`() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule
            .onNodeWithText(context.getString(R.string.app_name))
            .assertIsDisplayed()
    }
}
