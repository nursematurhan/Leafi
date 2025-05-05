package com.nursematurhan.leafi

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
import com.nursematurhan.leafi.worker.scheduleDailyReminder
import kotlinx.coroutines.delay
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleDailyReminder(this)

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
