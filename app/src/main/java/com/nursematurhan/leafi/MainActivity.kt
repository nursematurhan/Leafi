package com.nursematurhan.leafi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nursematurhan.leafi.ui.auth.AuthViewModel
import com.nursematurhan.leafi.ui.main.MainScreen
import com.nursematurhan.leafi.ui.splash.SplashScreen
import com.nursematurhan.leafi.ui.theme.LeafiTheme
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.nursematurhan.leafi.util.scheduleWaterReminder
import com.google.firebase.auth.FirebaseAuth
import com.nursematurhan.leafi.util.ReminderWorker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1002
                )
            }
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            scheduleWaterReminder(this)
        }


        setContent {
            LeafiTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val authViewModel: AuthViewModel = viewModel()
                    var splashShown by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(2000)
                        splashShown = true
                    }

                    if (!splashShown) {
                        SplashScreen()
                    } else {
                        MainScreen(authViewModel)
                    }
                }
            }
        }
    }
}
