package com.aidannemeth.weatherly.feature.weather.presentation.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidannemeth.weatherly.feature.common.presentation.theme.WeatherlyTheme
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.viewmodel.WeatherViewModel
import org.jetbrains.annotations.VisibleForTesting

@Composable
fun WeatherScreenContainer(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WeatherScreen(state)
}

@VisibleForTesting
@Composable
internal fun WeatherScreen(state: WeatherMetadataState) {
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
                    targetState = state,
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
                            fontSize = 160.sp,
                            fontWeight = FontWeight.Bold,
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
            }
        }
    }
}

@Preview(device = PIXEL_7_PRO)
@Preview(uiMode = UI_MODE_NIGHT_YES, device = PIXEL_7_PRO)
@Composable
fun WeatherScreenPreview() {
    WeatherlyTheme {
        WeatherScreen(
            state = WeatherMetadataState.Data(
                weatherUiModel = WeatherUiModel(
                    temperature = "100",
                )
            ),
        )
    }
}
