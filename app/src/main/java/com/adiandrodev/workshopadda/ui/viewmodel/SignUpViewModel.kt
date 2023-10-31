package com.adiandrodev.workshopadda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adiandrodev.workshopadda.data.firebase.AuthRepository
import com.adiandrodev.workshopadda.data.model.User

class SignUpViewModel(private val authRepository: AuthRepository): ViewModel() {

    fun registerUser(
        name: String,
        email: String,
        password: String,
        userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        authRepository.registerUser(name, email, password, userRegisterationResult)
    }

}

class SignUpViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(authRepository) as T
    }
}