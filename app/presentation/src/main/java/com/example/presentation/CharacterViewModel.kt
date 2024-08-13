package com.example.presentation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Character
import com.example.domain.GetCharactersUseCase
import kotlinx.coroutines.launch

class CharacterViewModel(private val getCharactersUseCase: GetCharactersUseCase) : ViewModel() {

    @VisibleForTesting
    val _uiEvent = mutableStateOf<Event>(Event.Loading)
    val uiEvent: State<Event>
        get() = _uiEvent

    init {
        _uiEvent.value = Event.Loading
        viewModelScope.launch {
            try {
                val characters = getCharactersUseCase.getCharacters() ?: error("getCharactersUseCase returned null")
                _uiEvent.value = Event.Success(characters)
            } catch (e: Exception) {
                // Handle errors
                _uiEvent.value = Event.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}

sealed class Event {
    data class Error(val message: String) : Event()
    data class Success(val data: List<Character>) : Event()
    object Loading : Event()
}
