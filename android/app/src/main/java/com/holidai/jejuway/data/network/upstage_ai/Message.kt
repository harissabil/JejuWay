package com.holidai.jejuway.data.network.upstage_ai

data class Message(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean
)
