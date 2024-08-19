package com.holidai.jejuway.data.network.upstage_ai

import com.holidai.jejuway.BuildConfig
import com.holidai.jejuway.data.network.upstage_ai.dto.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("v1/solar/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun getChatResponse(
        @Header("Authorization") token: String = "Bearer ${BuildConfig.UPSTAGE_AI_API_KEY}",
        @Body chatRequest: ChatRequest,
    ): ChatResponse
}