package com.nursematurhan.leafi.auth

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
}

val AuthState.isLoading: Boolean
    get() = this is AuthState.Loading

val AuthState.isSuccess: Boolean
    get() = this is AuthState.Success

val AuthState.error: String?
    get() = (this as? AuthState.Error)?.message