package com.holidai.jejuway.di

import com.holidai.jejuway.data.shared_pref.UserSession
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefModule = module {
    single { UserSession(androidContext()) }
}