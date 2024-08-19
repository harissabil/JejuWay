package com.holidai.jejuway.di

import com.holidai.jejuway.domain.usecases.GetWeather
import com.holidai.jejuway.domain.usecases.JsonToTestingLllm
import org.koin.dsl.module

val useCaseModule = module {
    single { JsonToTestingLllm() }
    single { GetWeather(get()) }
}