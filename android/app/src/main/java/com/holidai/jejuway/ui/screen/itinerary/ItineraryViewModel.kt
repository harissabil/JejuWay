package com.holidai.jejuway.ui.screen.itinerary

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.upstage_ai.ChatRequest
import com.holidai.jejuway.data.network.upstage_ai.Message
import com.holidai.jejuway.data.network.upstage_ai.UpstageAiRepository
import com.holidai.jejuway.data.network.upstage_ai.dto.TestingLlmResponse
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import com.holidai.jejuway.data.supabase.database.SupabaseDatabase
import com.holidai.jejuway.domain.models.itinerary.Review
import com.holidai.jejuway.domain.usecases.JsonToTestingLllm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ItineraryViewModel(
    private val supabaseAuth: SupabaseAuth,
    private val supabaseDatabase: SupabaseDatabase,
    private val upstageAiRepository: UpstageAiRepository,
) : ViewModel() {

    val itinerary = MutableStateFlow<TestingLlmResponse?>(null)

    private val _currentItineraryId = MutableStateFlow<String?>(null)

    val _currentScheduleId = MutableStateFlow<String?>(null)

    private val _currentContent = MutableStateFlow<String?>(null)
    val currentContent: StateFlow<String?> = _currentContent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reviewState = MutableStateFlow(ReviewState())
    val reviewState: StateFlow<ReviewState> = _reviewState.asStateFlow()

    fun onRatingChanged(rating: Float) {
        _reviewState.value = _reviewState.value.copy(rating = rating)
    }

    fun onReviewChanged(review: String) {
        _reviewState.value = _reviewState.value.copy(review = review)
    }

    fun onSubmitReview(
        context: Context,
    ) = viewModelScope.launch {
        if (_currentScheduleId.value == null) {
            Toast.makeText(context, "Please select a schedule", Toast.LENGTH_SHORT).show()
            return@launch
        }

        val response = getUserId()?.let {
            supabaseDatabase.insertReview(
                Review(
                    scheduleId = _currentScheduleId.value!!,
                    rating = _reviewState.value.rating,
                    review = _reviewState.value.review,
                )
            )
        }

        if (response is Resource.Success) {
            Toast.makeText(context, "Review submitted", Toast.LENGTH_SHORT).show()
        } else {
            if (response != null) {
                Timber.e(response.message)
            }
            Toast.makeText(
                context,
                "Failed to submit review: ${response?.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        _reviewState.value = ReviewState()
        _currentScheduleId.value = null

//        // dummy
//        delay(2000)
//        Toast.makeText(context, "Review submitted", Toast.LENGTH_SHORT).show()
//        _reviewState.value = ReviewState()
    }

    private suspend fun getUserId(): String? {
        return supabaseAuth.getUserId().data
    }

    fun setContent(content: String) {
        _currentContent.value = content
    }

    fun setItinerary(
        context: Context,
        itinerary: TestingLlmResponse,
    ) {
//        insertItinerary(context, itinerary)
        this.itinerary.value = itinerary
    }

    fun updateItinerary(
        context: Context,
        notes: String,
    ) = viewModelScope.launch {
        _isLoading.value = true

        val response = upstageAiRepository.getChatResponse(
            ChatRequest(
                model = "solar-1-mini-chat",
                messages = listOf(
                    Message(
                        role = "system",
                        content = "You are the travel guide and itinerary planner for this trip to Jeju Island! I already have the itinerary that you have generated previously, now I want to make an adjustment to that!"
                    ),
                    Message(
                        role = "user",
                        content = """
                            This is the itinerary that I have generated previously:
                            ${_currentContent.value.toString()}

                            I want to make an adjustment to this itinerary. Here are my personal preferences:
                            $notes

                            Can you help me to adjust the itinerary based on my personal preferences? You have to keep the unefected schedules and only adjust the schedules that are affected by my personal preferences.
                            Also keep in notes that you have to follow the json format as previous itinerary!

                            Please output the itinerary in the following JSON format, with multiple activities for each day:
                            {
                                "itinerary_id": "example_id",
                                "user_id": "example_id",
                                "start_date": "start date",
                                "end_date": "end date",
                                "number_of_people": "number of people",
                                "theme": "themes",
                                "notes": "notes",
                                "update_notes": "notes",
                                "created_at": "created at as previous itinerary",
                                "schedule": [
                                    {
                                      "id": "schedule-id-001",
                                      "itinerary_id": "unique-id-123",
                                      "order": 1,
                                      "date": "2024-09-01",
                                      "time": "08:00",
                                      "trip_day": 1,
                                      "activity_name": "Check-in at hotel",
                                      "place_name": "[Hotel Name]",
                                      "activity_category": "Hotel",
                                      "details": "Check-in at [Hotel Name]",
                                      "completed": false,
                                      "address": "[Hotel Address]",
                                      "lat": 33.4597,
                                      "long": 126.9426,
                                      "photoUrl": null
                                    },
                                    {
                                      "id": "schedule-id-002",
                                      "itinerary_id": "unique-id-123",
                                      "order": 2,
                                      "date": "2024-09-01",
                                      "time": "10:00",
                                      "trip_day": 1,
                                      "activity_name": "Visit Seongsan Ilchulbong Peak",
                                      "place_name": "Seongsan Ilchulbong Peak",
                                      "activity_category": "Nature",
                                      "details": "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
                                      "completed": false,
                                      "address": "Seongsan-eup, Seogwipo-si, Jeju",
                                      "lat": 33.4597,
                                      "long": 126.9426,
                                      "photoUrl": null
                                    },
                                    {
                                      "id": "schedule-id-003",
                                      "itinerary_id": "unique-id-123",
                                      "order": 3,
                                      "date": "2024-09-01",
                                      "time": "13:00",
                                      "trip_day": 1,
                                      "activity_name": "Lunch at Jeju Black Pork Restaurant",
                                      "place_name": "Jeju Black Pork Restaurant",
                                      "activity_category": "Food",
                                      "details": "Enjoy a traditional Jeju black pork BBQ.",
                                      "completed": false,
                                      "address": "Jeju-si, Jeju",
                                      "lat": 33.5124,
                                      "long": 126.5182,
                                      "photoUrl": null
                                    }
                                    // Continue with more activities for each day
                                ]
                            }
                            ""${'"'}
                        """.trimIndent()
                    )
                ),
                stream = false
            )
        )

        if (response.choices?.get(0)?.message?.content == null) {
            Toast.makeText(context, "Failed to update itinerary", Toast.LENGTH_SHORT).show()
            return@launch
        }

        response.choices.get(0)?.message?.content?.let {
            setContent(it)

            val jsonToTestingLllmm = JsonToTestingLllm().invoke(it)

            updateItineraryAndSchedules(context, notes, jsonToTestingLllmm)
        }

        // Dummy
//        delay(4000)
//        itinerary.value = DummyDataSource.provideAdjustmentForDayOneAndThree("")
//        _isLoading.value = false
    }

    private fun updateItineraryAndSchedules(
        context: Context,
        notes: String,
        testingLlmResponse: TestingLlmResponse,
    ) = viewModelScope.launch {
        val response = supabaseDatabase.updateItinerary(
            testingLlmResponse.toItinerary().copy(
                updateNotes = notes
            )
        )

        if (response is Resource.Success) {
            val scheduleResponse = supabaseDatabase.updateSchedule(
                itineraryId = response.data?.itineraryId!!,
                testingLlmResponse.schedule
            )

            if (scheduleResponse is Resource.Success) {
                Timber.d("Schedule updated: ${scheduleResponse.data}")
            } else {
                Timber.e(scheduleResponse.message)
                Toast.makeText(
                    context,
                    "Failed to update schedule: ${scheduleResponse.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Timber.e(response.message)
            Toast.makeText(
                context,
                "Failed to update itinerary: ${response.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        response.data?.itineraryId?.let {
            supabaseDatabase.getItinerary(it).let {
                val newTestingLlmResponse = it.data?.let { it1 ->
                    testingLlmResponse.copy(
                        itineraryId = response.data.itineraryId,
                        schedule = it1.schedule
                    )
                }
                itinerary.value = newTestingLlmResponse
            }
        }
    }

    private fun insertItinerary(
        context: Context,
        testingLlmResponse: TestingLlmResponse,
    ) = viewModelScope.launch {
        val response = getUserId()?.let {
            supabaseDatabase.insertItinerary(
                userId = it,
                testingLlmResponse.toItinerary()
            )
        }

        if (response is Resource.Success) {
            // change all itineraryIdd to the new one in list of schedules
            val schedules = testingLlmResponse.schedule.map {
                it.copy(itineraryId = response.data?.itineraryId)
            }
//            schedules.forEach {
//                Timber.d("Inserting schedule: $it")
//                supabaseDatabase.insertSchedule(it)
//            }
            val insertScehdulesResponse = supabaseDatabase.insertAllSchedule(schedules)

            if (insertScehdulesResponse is Resource.Success) {
                Toast.makeText(context, "Itinerary inserted", Toast.LENGTH_SHORT).show()
            } else {
                Timber.e(response.message)
                Toast.makeText(
                    context,
                    "Failed to insert schedules: ${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (response != null) {
                Timber.e(response.message)
            }
            Toast.makeText(
                context,
                "Failed to insert itinerary: ${response?.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (response != null) {
            supabaseDatabase.getItinerary(response.data?.itineraryId!!).let {
                val newTestingLlmResponse = it.data?.let { it1 ->
                    testingLlmResponse.copy(
                        itineraryId = response.data.itineraryId,
                        schedule = it1.schedule
                    )
                }
                itinerary.value = newTestingLlmResponse
            }
        }
    }

    private fun getItineraryFirst() = viewModelScope.launch {
        val response = getUserId()?.let {
            supabaseDatabase.getItinerary("722be058-ec25-44fb-bb9a-943684332927")
        }

        if (response is Resource.Success) {
            itinerary.value = response.data
        } else {
            if (response != null) {
                Timber.e(response.message)
            }
        }
    }

    fun getItineraryFromId(itineraryId: String) = viewModelScope.launch {
        supabaseDatabase.getItinerary(itineraryId = itineraryId).let {
            val newTestingLlmResponse = it.data
            itinerary.value = newTestingLlmResponse
        }
    }

//    init {
//        getItineraryFirst()
//    }
}