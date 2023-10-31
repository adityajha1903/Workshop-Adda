package com.adiandrodev.workshopadda.data.firebase

import com.adiandrodev.workshopadda.data.model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val firestore = FirestoreRepository()

    fun signOut() {
        auth.signOut()
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
        userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid, name, registeredEmail)
                    firestore.registerUser(user, userRegisterationResult)
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        userRegisterationResult.invoke(false, null, "Please create a strong password")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        userRegisterationResult.invoke(false, null, "Email address is badly formatted")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        userRegisterationResult.invoke(false, null, "This user already exist")
                    } catch (e: Exception) {
                        userRegisterationResult.invoke(false, null, e.message)
                    }
                }
            }
    }

    fun logInUser(
        email: String,
        password: String,
        userRegisterationResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestore.loadUserData(userRegisterationResult)
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        userRegisterationResult.invoke(false, null, "No such user exist.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        userRegisterationResult.invoke(false, null, "Wrong email or password entered.")
                    } catch (e: FirebaseNetworkException) {
                        userRegisterationResult.invoke(false, null, "Network unavailable.")
                    } catch (e: Exception) {
                        userRegisterationResult.invoke(false, null, e.message)
                    }
                }
            }
    }
}