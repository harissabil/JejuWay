package com.holidai.jejuway.ui.screen.home_navigator.trips

import com.holidai.jejuway.data.network.upstage_ai.dto.TestingLlmResponse

data class TripsState(
    val tripList: List<TestingLlmResponse> = emptyList(),
    val isLoading: Boolean = false,
)
