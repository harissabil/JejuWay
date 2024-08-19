package com.holidai.jejuway.ui.screen.home_navigator.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.supabase.database.SupabaseDatabase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TripsViewModel(
    private val supabaseDatabase: SupabaseDatabase,
) : ViewModel() {

    private val _state = MutableStateFlow(TripsState())
    val state: StateFlow<TripsState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        getAllItinerary()
    }

    private fun getAllItinerary() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val response = supabaseDatabase.getAllItinerary()
        when (response) {
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false) }
                _eventFlow.emit(UIEvent.ShowSnackbar(response.message ?: "Something went wrong"))
            }

            is Resource.Loading -> {
                _state.update { it.copy(isLoading = true) }
            }

            is Resource.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        tripList = response.data ?: emptyList()
                    )
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}