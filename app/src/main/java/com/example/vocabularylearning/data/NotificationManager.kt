package com.example.vocabularylearning.data

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vocabularylearning.worker.VocabularyNotificationWorker
import java.util.concurrent.TimeUnit

class NotificationManager(private val context: Context) {

    fun scheduleReminder() {
        val notificationWork = PeriodicWorkRequestBuilder<VocabularyNotificationWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "vocabulary_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWork
        )
    }

    fun scheduleImmediateReminderForTesting() {
        val notificationWork = OneTimeWorkRequestBuilder<VocabularyNotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(notificationWork)
    }

    fun cancelReminders() {
        WorkManager.getInstance(context).cancelUniqueWork("vocabulary_notification_work")
    }
}