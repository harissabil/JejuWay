package com.holidai.jejuway.data.network.weather

import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.weather.mapper.toWeather
import com.holidai.jejuway.domain.models.weather.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val apiService: ApiService,
) {
    fun getWeather(latitude: Double, longitude: Double): Flow<Resource<Weather>> =
        flow {
            emit(Resource.Loading())
            emit(Resource.Success(apiService.getWeather(latitude, longitude).toWeather()))
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
}