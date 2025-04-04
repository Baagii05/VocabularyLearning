package com.example.vocabularylearning.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val wordsRepository: WordsRepository
    val userPreferencesRepository: UserPreferencesRepository
    val notificationManager: NotificationManager
}


class AppDataContainer(private val context: Context) : AppContainer {

    override val wordsRepository: WordsRepository by lazy {
        OfflineWordsRepository(WordDatabase.getDatabase(context).wordDao())
    }


    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }


    override val notificationManager: NotificationManager by lazy {
        NotificationManager(context)
    }
}