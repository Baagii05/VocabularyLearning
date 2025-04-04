package com.example.vocabularylearning.ui.word


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularylearning.data.WordsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class WordEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordsRepository: WordsRepository
) : ViewModel() {

    private val wordId: Int = checkNotNull(savedStateHandle[WordEditDestination.wordIdArg])


    var wordUiState by mutableStateOf(WordUiState())
        private set

    init {
        viewModelScope.launch {
            wordUiState = wordsRepository.getWordStream(wordId)
                .filterNotNull()
                .first()
                .toWordUiState(true)
        }
    }

    fun updateUiState(wordDetails: WordDetails) {
        wordUiState =
            WordUiState(wordDetails = wordDetails, isEntryValid = validateInput(wordDetails))
    }

    suspend fun updateWord() {
        if (validateInput(wordUiState.wordDetails)) {
            wordsRepository.updateWord(wordUiState.wordDetails.toWord())
        }
    }

    private fun validateInput(uiState: WordDetails = wordUiState.wordDetails): Boolean {
        return with(uiState) {
            mongolian.isNotBlank() && english.isNotBlank()
        }
    }
}

