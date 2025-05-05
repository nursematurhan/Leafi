package com.nursematurhan.leafi.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.nursematurhan.leafi.R
import com.nursematurhan.leafi.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    var displayName by remember { mutableStateOf(user?.displayName ?: "") }
    var newPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("My Profile", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Name Surname") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val profileUpdates = userProfileChangeRequest {
                    displayName = displayName
                }
                user?.updateProfile(profileUpdates)
                    ?.addOnSuccessListener {
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        ) {
            Text("Update Name")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                user?.updatePassword(newPassword)
                    ?.addOnSuccessListener {
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        ) {
            Text("Update Password")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.logout() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Log Out")
        }
    }
}
