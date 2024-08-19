package com.holidai.jejuway.domain.usecases

import com.google.gson.Gson
import com.holidai.jejuway.data.network.upstage_ai.dto.TestingLlmResponse

class JsonToTestingLllm {
    operator fun invoke(jsonString: String): TestingLlmResponse {
        val gson = Gson()
        return gson.fromJson(jsonString, TestingLlmResponse::class.java)
    }
}