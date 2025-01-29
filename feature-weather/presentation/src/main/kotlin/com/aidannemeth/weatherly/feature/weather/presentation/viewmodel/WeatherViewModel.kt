package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.RefreshWeather
import com.aidannemeth.weatherly.feature.weather.presentation.mapper.toEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.reducer.WeatherReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val observeWeather: ObserveWeather,
    private val reducer: WeatherReducer,
    private val refreshWeather: RefreshWeather,
) : ViewModel() {
    private val mutableState: MutableStateFlow<WeatherState> =
        MutableStateFlow(WeatherState.Loading)

    val state: StateFlow<WeatherState> = mutableState.asStateFlow()

    init {
        observeWeatherMetadata()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeWeatherMetadata() =
        observeWeather()
            .mapLatest(Either<DataError, Weather>::toEvent)
            .onEach(::dispatch)
            .launchIn(viewModelScope)

    private fun dispatch(operation: WeatherOperation) =
        mutableState.update { currentState ->
            reducer.getState(operation, currentState)
        }

    internal fun dispatchAction(action: WeatherAction) =
        viewModelScope.launch {
            dispatch(
                operation = when (action) {
                    WeatherAction.RefreshWeather ->
                        refreshWeather().toEvent()
                }
            )
        }
}
