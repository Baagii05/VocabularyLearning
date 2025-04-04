package com.example.vocabularylearning.ui.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.vocabularylearning.data.Word
import com.example.vocabularylearning.data.WordsRepository

class WordEntryViewModel(private val wordsRepository: WordsRepository)  : ViewModel(){

    var wordUiState by mutableStateOf(WordUiState())
        private set

    fun updateUiState(wordDetails: WordDetails){
        wordUiState =
            WordUiState(wordDetails = wordDetails, isEntryValid = validateInput(wordDetails))
    }

    private fun validateInput(uiState: WordDetails = wordUiState.wordDetails): Boolean {
        return with(uiState) {
            mongolian.isNotBlank() && english.isNotBlank()
        }
    }

    suspend fun saveWord() {
        if (validateInput()) {
            wordsRepository.insertWord(wordUiState.wordDetails.toWord())
        }
    }

}

data class WordUiState(
    val wordDetails: WordDetails = WordDetails(),
    val isEntryValid: Boolean = false
)

data class WordDetails(
    val id: Int = 0,
    val mongolian: String = "",
    val english: String = "",

)

fun WordDetails.toWord(): Word = Word(
    id = id,
    mongolian = mongolian,
    english = english

)

fun Word.toWordUiState(isEntryValid: Boolean = false): WordUiState = WordUiState(
    wordDetails = this.toWordDetails(),
    isEntryValid = isEntryValid
)


fun Word.toWordDetails(): WordDetails = WordDetails(
    id = id,
    mongolian = mongolian,
    english = english
)

