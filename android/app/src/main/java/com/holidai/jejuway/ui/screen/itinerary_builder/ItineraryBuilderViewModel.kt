package com.holidai.jejuway.ui.screen.itinerary_builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.upstage_ai.UpstageAiRepository
import com.holidai.jejuway.data.network.weather.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItineraryBuilderViewModel(
    private val upstageAiRepository: UpstageAiRepository,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ItineraryBuilderState())
    val state: StateFlow<ItineraryBuilderState> = _state.asStateFlow()

    fun onTravelingWithKidsChanged(isTravelingWithKids: Boolean) {
        _state.value = _state.value.copy(isTravelingWithKids = isTravelingWithKids)
    }

    fun onTravelingWithPetsChanged(isTravelingWithPets: Boolean) {
        _state.value = _state.value.copy(isTravelingWithPets = isTravelingWithPets)
    }

    fun onPersonCountChanged(personCount: Int) {
        _state.value = _state.value.copy(personCount = personCount)
    }

    fun onNotesChanged(notes: String) {
        _state.value = _state.value.copy(notes = notes)
    }

    fun onGeneratingItinerary(
        startDate: String,
        endDate: String,
        themes: String,
    ) = viewModelScope.launch {
        if (_state.value.isGenerating) return@launch

        _state.value = _state.value.copy(isGenerating = true)

        val currentWeather = weatherRepository.getWeather(
            latitude = 33.4510,
            longitude = 126.5276,
        ).first()

        val weatherCondition = if (currentWeather is Resource.Success) {
            "The weather condition is ${currentWeather.data?.current?.weatherCode?.description}, with the temperature is ${currentWeather.data?.current?.temperature2m}"
        } else {
            ""
        }

        val travelingWithKids = if (_state.value.isTravelingWithKids) {
            "I'm traveling with kids"
        } else {
            "I'm not traveling with kids"
        }

        val travelingWithPets = if (_state.value.isTravelingWithPets) {
            "I'm traveling with pets"
        } else {
            "I'm not traveling with pets"
        }

        val preferedThemes = if (themes.isNotBlank()) {
            "I prefer the trip themes is $themes"
        } else {
            "I do not have any preferred themes"
        }

        val personalPreferences = if (_state.value.notes.isNotBlank()) {
            "I have personal preferences that you can includes: ${_state.value.notes}"
        } else {
            "I do not have any personal preferences to include so you can plan the trip as you see fit"
        }

        delay(5000)
        _state.update {
            it.copy(
                isGenerating = true,
                isSuccessful = true,
                content = "response.choices?.get(0)?.message?.content"
            )
        }

//        val response = upstageAiRepository.getChatResponse(
//            chatRequest = ChatRequest(
//                model = "solar-1-mini-chat",
//                messages = listOf(
//                    Message(
//                        role = "system",
//                        content = "You are the travel guide and itinerary planner for this trip to Jeju Island!"
//                    ),
//                    Message(
//                        role = "user",
//                        content = """
//                            I'm traveling to Jeju Island from $startDate to $endDate with ${_state.value.personCount} people.
//                            Please note that $travelingWithKids, $travelingWithPets, $preferedThemes, and $personalPreferences. $weatherCondition.
//                            I would like an itinerary that includes at least 3 activities per day, including:
//                            - Morning activities (like nature tours or visiting landmarks)
//                            - Afternoon activities (like visiting cultural spots or eating at recommended restaurants)
//                            - Evening activities (like shopping or entertainment spots)
//                            Make sure to also include check-in and check-out times at the hotel on relevant days.
//                            The activity category that you'll write in the output is only these string: Hotel, Transportation, Food, Nature, Culture, Shopping, Entertainment, City, amd Seaside..
//                            If you don't know the value, you can give it a null.
//
//                            Please output the itinerary in the following JSON format, with multiple activities for each day:
//                            {
//                                "itinerary_id": "example_id",
//                                "user_id": "example_id",
//                                "start_date": "$startDate",
//                                "end_date": "$endDate",
//                                "number_of_people": ${_state.value.personCount},
//                                "theme": "$themes",
//                                "notes": "${_state.value.notes}",
//                                "update_notes": null,
//                                "created_at": "2024-08-15",
//                                "schedule": [
//                                    {
//                                      "id": "schedule-id-001",
//                                      "itinerary_id": "unique-id-123",
//                                      "order": 1,
//                                      "date": "2024-09-01",
//                                      "time": "08:00",
//                                      "trip_day": 1,
//                                      "activity_name": "Check-in at hotel",
//                                      "place_name": "[Hotel Name]",
//                                      "activity_category": "Hotel",
//                                      "details": "Check-in at [Hotel Name]",
//                                      "completed": false,
//                                      "address": "[Hotel Address]",
//                                      "lat": 33.4597,
//                                      "long": 126.9426,
//                                      "photoUrl": null
//                                    },
//                                    {
//                                      "id": "schedule-id-002",
//                                      "itinerary_id": "unique-id-123",
//                                      "order": 2,
//                                      "date": "2024-09-01",
//                                      "time": "10:00",
//                                      "trip_day": 1,
//                                      "activity_name": "Visit Seongsan Ilchulbong Peak",
//                                      "place_name": "Seongsan Ilchulbong Peak",
//                                      "activity_category": "Nature",
//                                      "details": "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                                      "completed": false,
//                                      "address": "Seongsan-eup, Seogwipo-si, Jeju",
//                                      "lat": 33.4597,
//                                      "long": 126.9426,
//                                      "photoUrl": null
//                                    },
//                                    {
//                                      "id": "schedule-id-003",
//                                      "itinerary_id": "unique-id-123",
//                                      "order": 3,
//                                      "date": "2024-09-01",
//                                      "time": "13:00",
//                                      "trip_day": 1,
//                                      "activity_name": "Lunch at Jeju Black Pork Restaurant",
//                                      "place_name": "Jeju Black Pork Restaurant",
//                                      "activity_category": "Food",
//                                      "details": "Enjoy a traditional Jeju black pork BBQ.",
//                                      "completed": false,
//                                      "address": "Jeju-si, Jeju",
//                                      "lat": 33.5124,
//                                      "long": 126.5182,
//                                      "photoUrl": null
//                                    }
//                                    // Continue with more activities for each day
//                                ]
//                            }
//                            """
//                            .trimIndent()
//                    )
//                ),
//                stream = false
//            )
//        )
//
//        Timber.i(response.choices?.get(0)?.message?.content ?: "No response")
//
//        _state.update {
//            it.copy(
//                isGenerating = false,
//                isSuccessful = true,
//                content = response.choices?.get(0)?.message?.content
//            )
//        }
    }
}