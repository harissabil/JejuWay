package com.holidai.jejuway.ui.screen.itinerary_builder

data class ItineraryBuilderState(
    val isTravelingWithKids: Boolean = false,
    val isTravelingWithPets: Boolean = false,
    val personCount: Int = 1,
    val notes: String = "",
    val isGenerating: Boolean = false,
    val isSuccessful: Boolean = false,
    val content: String? = null
)