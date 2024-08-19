package com.holidai.jejuway.di

import com.holidai.jejuway.MainViewModel
import com.holidai.jejuway.ui.screen.auth.AuthViewModel
import com.holidai.jejuway.ui.screen.home.HomeViewModel
import com.holidai.jejuway.ui.screen.home_navigator.home_main.HomeMainViewModel
import com.holidai.jejuway.ui.screen.home_navigator.trips.TripsViewModel
import com.holidai.jejuway.ui.screen.itinerary.ItineraryViewModel
import com.holidai.jejuway.ui.screen.itinerary_builder.ItineraryBuilderViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
     viewModelOf(::HomeViewModel)
     viewModelOf(::MainViewModel)
     viewModelOf(::AuthViewModel)
     viewModelOf(::ItineraryBuilderViewModel)
     viewModelOf(::ItineraryViewModel)
     viewModelOf(::HomeMainViewModel)
     viewModelOf(::TripsViewModel)
}