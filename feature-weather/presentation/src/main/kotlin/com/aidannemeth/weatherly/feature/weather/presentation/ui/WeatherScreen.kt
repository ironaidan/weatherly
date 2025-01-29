@file:Suppress("FunctionNaming")

package com.aidannemeth.weatherly.feature.weather.presentation.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.common.presentation.model.string
import com.aidannemeth.weatherly.feature.common.presentation.theme.WeatherlyTheme
import com.aidannemeth.weatherly.feature.weather.presentation.R
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.viewmodel.WeatherViewModel
import org.jetbrains.annotations.VisibleForTesting

@Composable
fun WeatherScreenContainer(viewModel: WeatherViewModel = hiltViewModel()) {
    val actions = Weather.Actions(
        refreshWeather = { viewModel.dispatchAction(WeatherAction.RefreshWeather) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    WeatherScreen(
        state = state,
        actions = actions
    )
}

@VisibleForTesting
@Composable
internal fun WeatherScreen(
    actions: Weather.Actions,
    state: WeatherState,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("WeatherScreen"),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = state.weatherMetadataState,
                    label = "weather_screen_content"
                ) { targetState ->
                    when (targetState) {
                        WeatherMetadataState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.testTag("LoadingIndicator"),
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        is WeatherMetadataState.Data -> Text(
                            text = targetState.weatherUiModel.temperature.string(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        is WeatherMetadataState.Error ->
                            targetState.message.string().let { message ->
                                LaunchedEffect(targetState) {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                    }
                }
                Button(
                    onClick = actions.refreshWeather,
                ) {
                    Text(text = stringResource(R.string.cta_text))
                }
            }
        }
    }
}

object Weather {
    data class Actions(
        val refreshWeather: () -> Unit,
    ) {
        companion object {
            val Empty = Actions(
                refreshWeather = {},
            )
        }
    }
}

@Preview(device = PIXEL_7_PRO)
@Preview(device = PIXEL_7_PRO, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherScreenPreview() {
    WeatherlyTheme {
        WeatherScreen(
            actions = Weather.Actions.Empty,
            state = WeatherState(
                weatherMetadataState = WeatherMetadataState.Data(
                    weatherUiModel = WeatherMetadataUiModel(
                        temperature = TextUiModel("100℉"),
                    )
                ),
            ),
        )
    }
}
