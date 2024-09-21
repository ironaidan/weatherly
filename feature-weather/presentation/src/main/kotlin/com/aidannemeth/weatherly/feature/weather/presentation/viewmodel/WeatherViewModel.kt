package com.aidannemeth.weatherly.feature.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.usecase.ObserveWeather
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherUiModel
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
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val observeWeather: ObserveWeather,
    private val reducer: WeatherReducer,
) : ViewModel() {

    private val mutableState: MutableStateFlow<WeatherState> =
        MutableStateFlow(WeatherState.Loading)

    val state: StateFlow<WeatherState> = mutableState.asStateFlow()

    init {
        observeWeatherMetadata()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeWeatherMetadata() {
        observeWeather()
            .mapLatest(::toWeatherEvent)
            .onEach(::updateState)
            .launchIn(viewModelScope)
    }

    private fun toWeatherEvent(weather: Either<DataError, Weather>): WeatherEvent {
        return weather.fold(
            ifLeft = { WeatherEvent.ErrorLoadingWeather },
            ifRight = {
                WeatherEvent.WeatherData(
                    WeatherUiModel(
                        temperature = it.temperature.value.toString(),
                    )
                )
            },
        )
    }

    private fun updateState(weatherEvent: WeatherEvent) {
        mutableState.update { currentState ->
            reducer.dispatch(weatherEvent, currentState)
        }
    }
}
