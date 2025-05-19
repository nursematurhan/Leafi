package com.nursematurhan.leafi.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nursematurhan.leafi.R
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Log.d("ReminderWorker", "❌ No user logged in – skipping notification")
            return Result.success()
        }

        val userId = user.uid
        val firestore = FirebaseFirestore.getInstance()

        return try {
            val snapshot = firestore.collection("MyPlants")
                .document(userId)
                .collection("Plants")
                .get()
                .await()

            val now = System.currentTimeMillis()

            snapshot.documents.forEach { doc ->
                val name = doc.getString("plantName") ?: return@forEach
                val lastWatered = doc.getLong("lastWateredDate") ?: return@forEach
                val interval = doc.getLong("wateringIntervalDays") ?: return@forEach

                val daysSince = TimeUnit.MILLISECONDS.toDays(now - lastWatered)

                when {
                    daysSince == interval -> {
                        sendNotification("Time to water $name!", "$name needs watering today!")
                    }
                    daysSince > interval -> {
                        sendNotification("You forgot to water $name", "$name was supposed to be watered ${daysSince - interval} day(s) ago.")
                    }
                    else -> {
                        Log.d("ReminderWorker", "$name does not need watering yet.")
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "❌ Error checking watering schedule", e)
            Result.failure()
        }
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "watering_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Watering Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val hasPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                Log.w("ReminderWorker", "❌ Notification permission denied.")
                return
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext).notify((0..10000).random(), notification)
        Log.d("ReminderWorker", "✅ Notification sent: $title")
    }
}
