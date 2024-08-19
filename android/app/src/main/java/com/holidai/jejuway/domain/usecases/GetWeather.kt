package com.holidai.jejuway.domain.usecases

import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.weather.WeatherRepository
import com.holidai.jejuway.domain.models.weather.Weather
import kotlinx.coroutines.flow.Flow

class GetWeather(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(latitude: Double, longitude: Double): Flow<Resource<Weather>> {
        return weatherRepository.getWeather(latitude, longitude)
    }
}