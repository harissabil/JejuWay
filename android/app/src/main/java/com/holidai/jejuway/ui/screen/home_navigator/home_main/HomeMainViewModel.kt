package com.holidai.jejuway.ui.screen.home_navigator.home_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.upstage_ai.ChatRequest
import com.holidai.jejuway.data.network.upstage_ai.Message
import com.holidai.jejuway.data.network.upstage_ai.UpstageAiRepository
import com.holidai.jejuway.domain.usecases.GetWeather
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeMainViewModel(
    private val getWeather: GetWeather,
    private val upstageAiRepository: UpstageAiRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeMainState())
    val state: StateFlow<HomeMainState> = _state.asStateFlow()

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        getWeatherVm()
    }

    private fun getWeatherVm() = viewModelScope.launch {
        getWeather.invoke(
            latitude = 33.4510,
            longitude = 126.5276,
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _weatherState.value = _weatherState.value.copy(isLoading = true)
                }

                is Resource.Error -> {
                    Timber.tag("HomeViewModel").e(it.message)
                    _weatherState.value = _weatherState.value.copy(isLoading = false)
                    _state.value =
                        _state.value.copy(isLoading = false)
                    _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Something went wrong"))
                }

                is Resource.Success -> {
                    Timber.tag("HomeViewModel").d("Weather: ${it.data}")
                    _weatherState.value =
                        _weatherState.value.copy(weather = it.data, isLoading = false)
                    _state.value = _state.value.copy(isLoading = false)

                    val suggestionFromAiAboutWeatherResponse = upstageAiRepository.getChatResponse(
                        ChatRequest(
                            model = "solar-1-mini-chat",
                            messages = listOf(
                                Message(
                                    role = "system",
                                    content = "You are a travel guide and itinerary planner for a trip to Jeju Island. I will provide you with detailed hourly weather information, and you should give recommendations in one concise paragraph."
                                ),
                                Message(
                                    role = "user",
                                    content = """
                    Based on the current and hourly forecasted weather in Jeju Island, what activities and preparations do you recommend?
                    Here is the weather information:
                    - Current: ${weatherState.value.weather?.current?.weatherCode?.description}, ${weatherState.value.weather?.current?.temperature2m}°C
                    - Hourly forecast:
                      Times: ${weatherState.value.weather?.hourly?.time?.joinToString(", ")},
                      Temperatures: ${
                                        weatherState.value.weather?.hourly?.temperature2m?.joinToString(
                                            "°C, "
                                        )
                                    }°C,
                      Weather codes: ${weatherState.value.weather?.hourly?.weatherCode?.joinToString { it?.description ?: "N/A" }}
                    Please provide your recommendations in one short paragraph.
                    Example response format: "Bring a sunscreen for the day as temperatures will be warm in the afternoon, but also pack a light jacket for cooler evening hours. Consider indoor activities during potential rain in the evening."
          
                """.trimIndent()
                                )
                            ),
                            stream = false
                        )
                    )

                    _weatherState.update {
                        it.copy(
                            recommendation = suggestionFromAiAboutWeatherResponse.choices?.get(0)?.message?.content
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}