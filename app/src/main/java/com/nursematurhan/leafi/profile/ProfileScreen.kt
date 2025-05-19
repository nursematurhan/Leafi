package com.nursematurhan.leafi.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nursematurhan.leafi.auth.AuthViewModel

@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    var displayName by remember { mutableStateOf(user?.displayName ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    fun saveUserProfileToFirestore(name: String) {
        val uid = user?.uid ?: return
        val data = mapOf(
            "displayName" to name,
            "email" to user.email,
            "uid" to uid,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        db.collection("users").document(uid)
            .set(data, SetOptions.merge())
    }

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
                        saveUserProfileToFirestore(displayName) // ✅ Firestore'a da kaydet
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
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (user?.email != null) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            user.updatePassword(newPassword)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Re-authentication failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        ) {
            Text("Update Password")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.logout(context) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Log Out")
        }
    }
}