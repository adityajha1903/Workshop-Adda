package com.adiandrodev.workshopadda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adiandrodev.workshopadda.data.firebase.AuthRepository
import com.adiandrodev.workshopadda.data.model.User

class LogInViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun logInUser(
        email: String,
        password: String,
        userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        authRepository.logInUser(email, password, userRegisterationResult)
    }
}

class LogInViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogInViewModel(authRepository) as T
    }
}