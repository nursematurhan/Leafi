

package com.nursematurhan.leafi.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleDailyReminder(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
        1, TimeUnit.DAYS
    )
        .setInitialDelay(1, TimeUnit.MINUTES) // test için 1 dk sonra başlat
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
