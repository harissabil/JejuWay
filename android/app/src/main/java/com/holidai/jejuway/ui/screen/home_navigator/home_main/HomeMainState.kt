package com.holidai.jejuway.ui.screen.home_navigator.home_main

import com.holidai.jejuway.domain.models.weather.Weather

data class HomeMainState(
    val isLoading: Boolean = false,
)

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val recommendation: String? = null,
)
