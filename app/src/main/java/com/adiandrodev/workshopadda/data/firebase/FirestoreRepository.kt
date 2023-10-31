package com.adiandrodev.workshopadda.data.firebase

import com.adiandrodev.workshopadda.util.Constants
import com.adiandrodev.workshopadda.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(
        user: User,
        userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                userRegisterationResult.invoke(true, user, null)
            }
            .addOnFailureListener { e ->
                userRegisterationResult.invoke(false, null, e.message)
            }
    }

    fun loadUserData(userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)
                userRegisterationResult.invoke(true, loggedUser, null)
            }.addOnFailureListener { e ->
                userRegisterationResult.invoke(true, null, e.message)
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (currentUser != null) {
            userId = currentUser.uid
        }
        return userId
    }

    fun updateUserData(userHashMap: HashMap<String, Any>, userUpdateResult: (isSuccess: Boolean, assignedWorkshopsIdsString: String?) -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                userUpdateResult.invoke(true, (userHashMap[Constants.ASSIGNED_WORKSHOPS_IDS_STRING]?:"").toString())
            }.addOnFailureListener { _ ->
                userUpdateResult.invoke(false, null)
            }
    }
}