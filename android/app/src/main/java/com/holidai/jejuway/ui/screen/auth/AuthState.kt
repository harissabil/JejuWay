package com.holidai.jejuway.ui.screen.auth

data class AuthState(
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerFullName: String = "",
    val loginEmail: String = "",
    val loginPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
)
