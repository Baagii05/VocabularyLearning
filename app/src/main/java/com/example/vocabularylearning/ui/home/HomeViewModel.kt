package com.example.vocabularylearning.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularylearning.data.UserPreferencesRepository
import com.example.vocabularylearning.data.Word
import com.example.vocabularylearning.data.WordDisplayMode
import com.example.vocabularylearning.data.WordsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeViewModel(
    private val wordsRepository: WordsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    val homeUiState: StateFlow<HomeUiState> = combine(
        wordsRepository.getAllWordsStream(),
        userPreferencesRepository.displayModeFlow
    ) { wordList, displayMode ->
        HomeUiState(wordList, displayMode)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HomeUiState()
    )

    fun deleteWord(word: Word) {
        viewModelScope.launch {
            wordsRepository.deleteWord(word)
        }
    }

    fun updateDisplayMode(displayMode: WordDisplayMode) {
        viewModelScope.launch {
            userPreferencesRepository.updateDisplayMode(displayMode)
        }
    }
}


data class HomeUiState(
    val wordList: List<Word> = listOf(),
    val displayMode: WordDisplayMode = WordDisplayMode.BOTH
)