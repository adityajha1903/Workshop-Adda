package com.adiandrodev.workshopadda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.workshopadda.data.db.WorkshopRepository
import com.adiandrodev.workshopadda.data.firebase.AuthRepository
import com.adiandrodev.workshopadda.data.firebase.FirestoreRepository
import com.adiandrodev.workshopadda.data.model.User
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val workshopRepository: WorkshopRepository,
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
): ViewModel()  {

    fun getCurrentUserId(): String {
        return firestoreRepository.getCurrentUserId()
    }

    fun updateUserData(
        userHashMap: HashMap<String, Any>,
        userUpdateResult: (isSuccess: Boolean, assignedWorkshopsIdsString: String?) -> Unit
    ) {
        firestoreRepository.updateUserData(userHashMap, userUpdateResult)
    }

    fun loadUserData(
            userLoadingResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        firestoreRepository.loadUserData(userLoadingResult)
    }

    fun getAllWorkshops() = liveData(Dispatchers.IO) {
        emit(workshopRepository.getAllWorkshops())
    }

    fun signOut() {
        authRepository.signOut()
    }
}

class MainViewModelFactory(
    private val workshopRepository: WorkshopRepository,
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(workshopRepository, firestoreRepository, authRepository) as T
    }
}