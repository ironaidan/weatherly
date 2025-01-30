package com.aidannemeth.weatherly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aidannemeth.weatherly.feature.common.presentation.theme.WeatherlyTheme
import com.aidannemeth.weatherly.navigation.Home
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        enableEdgeToEdge()
        setContent {
            WeatherlyTheme {
                Home()
            }
        }
    }
}
