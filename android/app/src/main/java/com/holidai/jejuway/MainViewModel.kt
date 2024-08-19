package com.holidai.jejuway

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import com.holidai.jejuway.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val supabaseAuth: SupabaseAuth,
) : ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestionation by mutableStateOf<Screen>(Screen.MainNavigation)
        private set

    init {
        getAppEntry()
    }

    private fun getAppEntry() = viewModelScope.launch {
        val isLoggedIn = supabaseAuth.isLoggedIn().data ?: false
        startDestionation = if (isLoggedIn) {
            Screen.MainNavigation
        } else {
            Screen.Auth
        }
        delay(300)
        splashCondition = false
    }
}