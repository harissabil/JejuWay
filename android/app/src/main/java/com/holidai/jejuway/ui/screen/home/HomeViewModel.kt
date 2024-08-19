package com.holidai.jejuway.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import kotlinx.coroutines.launch

class HomeViewModel(
    private val supabaseAuth: SupabaseAuth
) : ViewModel() {

    fun logout() = viewModelScope.launch {
        supabaseAuth.logout()
    }
}