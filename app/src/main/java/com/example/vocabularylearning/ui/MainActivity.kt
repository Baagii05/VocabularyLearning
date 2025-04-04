package com.example.vocabularylearning.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Modifier
import com.example.vocabularylearning.VocabularyApp
import com.example.vocabularylearning.WordApplication

import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.vocabularylearning.ui.theme.VocabularyLearningTheme

class MainActivity : ComponentActivity() {


    private val notificationManager by lazy {
        (application as WordApplication).container.notificationManager
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        setContent {
            VocabularyLearningTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val windowSize = calculateWindowSizeClass(this)
                    VocabularyApp(
                        windowSize = windowSize.widthSizeClass
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        notificationManager.scheduleReminder()
        notificationManager.scheduleImmediateReminderForTesting()
    }
}
