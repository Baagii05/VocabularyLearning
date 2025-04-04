package com.example.vocabularylearning.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularylearning.data.UserPreferencesRepository
import com.example.vocabularylearning.data.WordDisplayMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DisplaySettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    val uiState: StateFlow<DisplaySettingsUiState> = userPreferencesRepository.displayModeFlow
        .map { displayMode ->
            DisplaySettingsUiState(displayMode)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DisplaySettingsUiState()
        )

    fun updateDisplayMode(displayMode: WordDisplayMode) {
        viewModelScope.launch {
            userPreferencesRepository.updateDisplayMode(displayMode)
        }
    }
}

data class DisplaySettingsUiState(
    val displayMode: WordDisplayMode = WordDisplayMode.BOTH
)