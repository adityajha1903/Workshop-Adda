package com.example.workshopadda.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.workshopadda.activities.LogInActivity
import com.example.workshopadda.activities.MainActivity
import com.example.workshopadda.activities.SignUpActivity
import com.example.workshopadda.fragments.WorkshopsFragment
import com.example.workshopadda.models.User
import com.example.workshopadda.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.math.log

class Firestore {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (currentUser != null) {
            userId = currentUser.uid
        }
        return userId
    }

    fun registerUser(activity: SignUpActivity, user: User) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistered(user)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                e.message?.let { activity.showErrorSnackBar(it) }
            }
    }

    fun loadUserData(activity: LogInActivity) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)
                if (loggedUser != null) {
                    activity.logInSuccess(loggedUser)
                }
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                e.message?.let { activity.showErrorSnackBar(it) }
            }
    }

    fun updateUserData(fragment: WorkshopsFragment, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                fragment.userUpdated()
            }.addOnFailureListener { e ->
                fragment.userUpdateFailed()
                Log.e(fragment.tag, e.message, e)
            }
    }

    fun loadUserDataFromFragment(fragment: WorkshopsFragment) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)
                if (loggedUser != null) {
                    fragment.userLoaded(loggedUser)
                }
            }.addOnFailureListener { e ->
                fragment.userLoadingFailed()
                Log.e(fragment.tag, e.message, e)
            }
    }
}