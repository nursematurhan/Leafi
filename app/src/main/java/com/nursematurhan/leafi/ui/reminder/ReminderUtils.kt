package com.nursematurhan.leafi.util

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleWaterReminder(context: Context) {
    val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
        .addTag("WaterReminderWork")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "WaterReminderWork",
        androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

fun cancelWaterReminder(context: Context) {
    WorkManager.getInstance(context).cancelUniqueWork("WaterReminderWork")
}
