package com.example.vocabularylearning.data

import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    fun getAllWordsStream(): Flow<List<Word>>

    fun getWordStream(id: Int): Flow<Word?>

    suspend fun insertWord(word: Word)


    suspend fun deleteWord(word: Word)

    suspend fun updateWord(word: Word)

}