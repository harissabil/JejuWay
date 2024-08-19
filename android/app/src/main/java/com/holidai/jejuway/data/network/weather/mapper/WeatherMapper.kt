package com.holidai.jejuway.data.network.weather.mapper

import com.holidai.jejuway.data.network.weather.dto.Current
import com.holidai.jejuway.data.network.weather.dto.CurrentUnits
import com.holidai.jejuway.data.network.weather.dto.Daily
import com.holidai.jejuway.data.network.weather.dto.DailyUnits
import com.holidai.jejuway.data.network.weather.dto.Hourly
import com.holidai.jejuway.data.network.weather.dto.HourlyUnits
import com.holidai.jejuway.data.network.weather.dto.WeatherResponse
import com.holidai.jejuway.domain.models.weather.Weather
import com.holidai.jejuway.domain.models.weather.WeatherCode.Companion.findByCode

internal fun WeatherResponse.toWeather() = Weather(
    elevation = this.elevation,
    hourlyUnits = this.hourlyUnits?.toHourlyUnits(),
    generationtimeMs = this.generationtimeMs,
    timezoneAbbreviation = this.timezoneAbbreviation,
    timezone = this.timezone,
    currentUnits = this.currentUnits?.toCurrentUnits(),
    latitude = this.latitude,
    utcOffsetSeconds = this.utcOffsetSeconds,
    hourly = this.hourly?.toHourly(),
    current = this.current?.toCurrent(),
    longitude = this.longitude,
    daily = this.daily?.toDaily(),
    dailyUnits = this.dailyUnits?.toDailyUnits()
)

internal fun HourlyUnits.toHourlyUnits() =
    com.holidai.jejuway.domain.models.weather.HourlyUnits(
        temperature2m = this.temperature2m,
        time = this.time,
        weatherCode = this.weatherCode
    )

internal fun CurrentUnits.toCurrentUnits() =
    com.holidai.jejuway.domain.models.weather.CurrentUnits(
        pressureMsl = this.pressureMsl,
        windSpeed10m = this.windSpeed10m,
        temperature2m = this.temperature2m,
        precipitation = this.precipitation,
        relativeHumidity2m = this.relativeHumidity2m,
        isDay = this.isDay,
        interval = this.interval,
        time = this.time,
        windDirection10m = this.windDirection10m,
        weatherCode = this.weatherCode
    )

internal fun DailyUnits.toDailyUnits() =
    com.holidai.jejuway.domain.models.weather.DailyUnits(
        temperature2mMax = this.temperature2mMax,
        temperature2mMin = this.temperature2mMin,
        time = this.time,
        weatherCode = this.weatherCode
    )

internal fun Hourly.toHourly() =
    com.holidai.jejuway.domain.models.weather.Hourly(
        temperature2m = this.temperature2m,
        time = this.time,
        weatherCode = this.weatherCode?.map { weatherCode ->
            weatherCode?.findByCode()
        }
    )

internal fun Current.toCurrent() =
    com.holidai.jejuway.domain.models.weather.Current(
        pressureMsl = this.pressureMsl,
        windSpeed10m = this.windSpeed10m,
        temperature2m = this.temperature2m,
        precipitation = this.precipitation,
        relativeHumidity2m = this.relativeHumidity2m,
        isDay = this.isDay != 0,
        interval = this.interval,
        time = this.time,
        windDirection10m = this.windDirection10m,
        weatherCode = this.weatherCode?.findByCode()
    )

internal fun Daily.toDaily() =
    com.holidai.jejuway.domain.models.weather.Daily(
        temperature2mMax = this.temperature2mMax,
        temperature2mMin = this.temperature2mMin,
        time = this.time,
        weatherCode = this.weatherCode?.map { weatherCode ->
            weatherCode?.findByCode()
        }
    )