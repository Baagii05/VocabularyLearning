package com.example.vocabularylearning

import android.app.Application
import com.example.vocabularylearning.data.AppContainer
import com.example.vocabularylearning.data.AppDataContainer

class WordApplication : Application() {


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}