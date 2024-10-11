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
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidannemeth.weatherly.feature.common.presentation.theme.WeatherlyTheme
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.viewmodel.WeatherViewModel
import org.jetbrains.annotations.VisibleForTesting

@Composable
fun WeatherScreenContainer(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WeatherScreen(
        state = state,
        actions = WeatherScreen.Actions(
            refreshWeather = { viewModel.dispatchAction(WeatherAction.RefreshWeather) }
        )
    )
}

@VisibleForTesting
@Composable
internal fun WeatherScreen(
    state: WeatherState,
    actions: WeatherScreen.Actions,
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
                            text = targetState.weatherUiModel.temperature,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        is WeatherMetadataState.Error ->
                            LaunchedEffect(targetState) {
                                snackbarHostState.showSnackbar(
                                    message = targetState.message,
                                )
                            }
                    }
                }
                Button(
                    onClick = actions.refreshWeather,
                    enabled = state.isRefreshing.not(),
                ) {
                    Text(text = "Refresh")
                }
            }
        }
    }
}

object WeatherScreen {

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
@Preview(uiMode = UI_MODE_NIGHT_YES, device = PIXEL_7_PRO)
@Composable
fun WeatherScreenPreview() {
    WeatherlyTheme {
        WeatherScreen(
            state = WeatherState(
                isRefreshing = false,
                (WeatherMetadataState.Data(
                    weatherUiModel = WeatherMetadataUiModel(
                        temperature = "100℉",
                    )
                )),
            ),
            actions = WeatherScreen.Actions.Empty,
        )
    }
}
