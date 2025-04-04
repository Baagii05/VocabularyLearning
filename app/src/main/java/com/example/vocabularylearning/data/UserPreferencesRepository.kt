package com.example.vocabularylearning.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


enum class WordDisplayMode(val value: Int) {
    BOTH(0),
    FOREIGN_ONLY(1),
    MONGOLIAN_ONLY(2)
}

class UserPreferencesRepository(private val context: Context) {

    companion object {

        private val DISPLAY_MODE_KEY = intPreferencesKey("display_mode")
    }


    val displayModeFlow: Flow<WordDisplayMode> = context.dataStore.data.map { preferences ->
        val displayModeValue = preferences[DISPLAY_MODE_KEY] ?: WordDisplayMode.BOTH.value
        WordDisplayMode.values().find { it.value == displayModeValue } ?: WordDisplayMode.BOTH
    }


    suspend fun updateDisplayMode(displayMode: WordDisplayMode) {
        context.dataStore.edit { preferences ->
            preferences[DISPLAY_MODE_KEY] = displayMode.value
        }
    }
}