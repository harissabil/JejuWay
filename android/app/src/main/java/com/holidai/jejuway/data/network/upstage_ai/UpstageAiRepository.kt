package com.holidai.jejuway.data.network.upstage_ai

import com.holidai.jejuway.data.network.upstage_ai.dto.ChatResponse

class UpstageAiRepository(
    private val apiService: ApiService,
) {

    suspend fun getChatResponse(chatRequest: ChatRequest): ChatResponse {
        return apiService.getChatResponse(chatRequest = chatRequest)
    }
}