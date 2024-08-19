package com.holidai.jejuway.di

import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    operator fun invoke(): Module = module {
        includes(
            listOf(
                viewModelModule,
                useCaseModule,
                sharedPrefModule,
                supabaseModule,
                weatherModule,
                weatherRepositoryModule,
                upstageAiModule,
                upstageAiRepositoryModule,
            )
        )
    }
}