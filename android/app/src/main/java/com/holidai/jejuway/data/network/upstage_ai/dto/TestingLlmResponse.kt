package com.holidai.jejuway.data.network.upstage_ai.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("itinerary")
data class TestingLlmResponse(

    @field:SerializedName("itinerary_id")
    @SerialName("itinerary_id")
    val itineraryId: String? = null,

    @field:SerializedName("user_id")
    @SerialName("user_id")
    val userId: String,

    @field:SerializedName("created_at")
    @SerialName("created_at")
    val createdAt: String,

    @field:SerializedName("start_date")
    @SerialName("start_date")
    val startDate: String,

    @field:SerializedName("end_date")
    @SerialName("end_date")
    val endDate: String,

    @field:SerializedName("number_of_people")
    @SerialName("number_of_people")
    val numberOfPeople: Int,

    @field:SerializedName("theme")
    @SerialName("theme")
    val theme: String,

    @field:SerializedName("notes")
    @SerialName("notes")
    val notes: String,

    @field:SerializedName("update_notes")
    @SerialName("update_notes")
    val updateNotes: String? = null,

    @field:SerializedName("schedule")
    @SerialName("schedule")
    val schedule: List<ScheduleItem>,
) {
    fun toItinerary(): Itinerary {
        return Itinerary(
            itineraryId = this.itineraryId,
            userId = this.userId,
            createdAt = this.createdAt,
            startDate = this.startDate,
            endDate = this.endDate,
            numberOfPeople = this.numberOfPeople,
            theme = this.theme,
            notes = this.notes,
            updateNotes = this.updateNotes,
        )
    }
}

@Serializable
@SerialName("schedule")
data class ScheduleItem(

    @field:SerializedName("id")
    @SerialName("id")
    val id: String?,

    @field:SerializedName("itinerary_id")
    @SerialName("itinerary_id")
    val itineraryId: String?,

    @field:SerializedName("trip_day")
    @SerialName("trip_day")
    val tripDay: Int,

    @field:SerializedName("date")
    @SerialName("date")
    val date: String,

    @field:SerializedName("activity_name")
    @SerialName("activity_name")
    val activityName: String,

    @field:SerializedName("address")
    @SerialName("address")
    val address: String?,

    @field:SerializedName("place_name")
    @SerialName("place_name")
    val placeName: String,

    @field:SerializedName("activity_category")
    @SerialName("activity_category")
    val activityCategory: String,

    @field:SerializedName("completed")
    @SerialName("completed")
    val completed: Boolean,

    @field:SerializedName("photo_url")
    @SerialName("photo_url")
    val photo_url: String?,

    @field:SerializedName("details")
    @SerialName("details")
    val details: String,

    @field:SerializedName("time")
    @SerialName("time")
    val time: String,

    @field:SerializedName("lat")
    @SerialName("lat")
    val lat: Double?,

    @field:SerializedName("long")
    @SerialName("long")
    val long: Double?,

    @field:SerializedName("order")
    @SerialName("order")
    val order: Int,

    @field:SerializedName("rating")
    @SerialName("rating")
    val rating: Double? = null,

    @field:SerializedName("review")
    @SerialName("review")
    val review: String? = null,
)

@Serializable
@SerialName("itinerary")
data class Itinerary(

    @field:SerializedName("itinerary_id")
    @SerialName("itinerary_id")
    val itineraryId: String? = null,

    @field:SerializedName("user_id")
    @SerialName("user_id")
    val userId: String,

    @field:SerializedName("created_at")
    @SerialName("created_at")
    val createdAt: String,

    @field:SerializedName("start_date")
    @SerialName("start_date")
    val startDate: String,

    @field:SerializedName("end_date")
    @SerialName("end_date")
    val endDate: String,

    @field:SerializedName("number_of_people")
    @SerialName("number_of_people")
    val numberOfPeople: Int,

    @field:SerializedName("theme")
    @SerialName("theme")
    val theme: String,

    @field:SerializedName("notes")
    @SerialName("notes")
    val notes: String,

    @field:SerializedName("update_notes")
    @SerialName("update_notes")
    val updateNotes: String? = null,
)
