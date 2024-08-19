package com.holidai.jejuway.ui.screen.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val supabaseAuth: SupabaseAuth,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onRegisterEmailChanged(email: String) {
        _state.value = _state.value.copy(registerEmail = email)
    }

    fun onRegisterPasswordChanged(password: String) {
        _state.value = _state.value.copy(registerPassword = password)
    }

    fun onRegisterFullNameChanged(fullName: String) {
        _state.value = _state.value.copy(registerFullName = fullName)
    }

    fun onLoginEmailChanged(email: String) {
        _state.value = _state.value.copy(loginEmail = email)
    }

    fun onLoginPasswordChanged(password: String) {
        _state.value = _state.value.copy(loginPassword = password)
    }

    fun register(context: Context) = viewModelScope.launch {
        if (_state.value.isLoading) return@launch

        if (_state.value.registerEmail.isBlank() || _state.value.registerPassword.isBlank() || _state.value.registerFullName.isBlank()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return@launch
        }

        _state.update { it.copy(isLoading = true) }
        val result = supabaseAuth.register(
            _state.value.registerEmail,
            _state.value.registerPassword,
            _state.value.registerFullName
        )

        val resultData = result.data
        _state.update { it.copy(isLoading = false) }
        resultData?.let { success ->
            if (success) {
                _state.update { it.copy(isSuccessful = true) }
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
            } else {
                Timber.e(result.message)
                Toast.makeText(context, "Registration failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(context: Context) = viewModelScope.launch {
        if (_state.value.isLoading) return@launch

        if (_state.value.loginEmail.isBlank() || _state.value.loginPassword.isBlank()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return@launch
        }

        _state.update { it.copy(isLoading = true) }
        val result = supabaseAuth.login(
            _state.value.loginEmail,
            _state.value.loginPassword
        )

        val resultData = result.data
        _state.update { it.copy(isLoading = false) }
        resultData?.let { success ->
            if (success) {
                _state.update { it.copy(isSuccessful = true) }
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
            } else {
                Timber.e(result.message)
                Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Timber.e(result.message)
            Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
        }
    }
}