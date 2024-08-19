package com.holidai.jejuway.domain.models.itinerary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("review")
data class Review(
    val id: Int? = null,

    @SerialName("user_id")
    val userId: String? = null,

    @SerialName("schedule_id")
    val scheduleId: String,

    @SerialName("created_at")
    val createdAt: String? = null,
    val rating: Float,
    val review: String,
)
