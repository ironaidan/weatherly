@file:Suppress("FunctionNaming")

package com.aidannemeth.weatherly.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aidannemeth.weatherly.navigation.model.Destination.Screen.Weather
import com.aidannemeth.weatherly.navigation.route.addWeatherScreen

@Composable
fun Home() {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Weather,
            ) {
                addWeatherScreen()
            }
        }
    }
}
