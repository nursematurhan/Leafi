package com.nursematurhan.leafi.ui.main

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nursematurhan.leafi.ui.auth.AuthState
import com.nursematurhan.leafi.ui.auth.AuthViewModel
import com.nursematurhan.leafi.ui.navigation.AppNavGraph
import androidx.compose.foundation.layout.padding
import com.nursematurhan.leafi.ui.navigation.BottomNavigationBar

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    val isLoggedIn = authState is AuthState.Success

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                isLoggedIn = isLoggedIn
            )
        }
    ) { paddingValues ->
        AppNavGraph(
            navController = navController,
            authViewModel = authViewModel,
            modifier = Modifier.padding(paddingValues),
            isLoggedIn = isLoggedIn
        )
    }
}
