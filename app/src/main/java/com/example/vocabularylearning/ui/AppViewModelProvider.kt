package com.example.vocabularylearning.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vocabularylearning.WordApplication
import com.example.vocabularylearning.ui.home.HomeViewModel
import com.example.vocabularylearning.ui.settings.DisplaySettingsViewModel
import com.example.vocabularylearning.ui.word.WordEditViewModel
import com.example.vocabularylearning.ui.word.WordEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WordEditViewModel(
                this.createSavedStateHandle(),
                wordApplication().container.wordsRepository
            )
        }

        initializer {
            WordEntryViewModel(wordApplication().container.wordsRepository)
        }

        initializer {
            HomeViewModel(
                wordApplication().container.wordsRepository,
                wordApplication().container.userPreferencesRepository
            )
        }

        initializer {
            DisplaySettingsViewModel(
                wordApplication().container.userPreferencesRepository
            )
        }
    }
}

fun CreationExtras.wordApplication(): WordApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as WordApplication)