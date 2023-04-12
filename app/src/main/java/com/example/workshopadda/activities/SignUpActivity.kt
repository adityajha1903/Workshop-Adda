package com.example.workshopadda.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import com.example.workshopadda.R
import com.example.workshopadda.databinding.ActivitySignUpBinding
import com.example.workshopadda.firebase.Firestore
import com.example.workshopadda.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpToolBar()

        binding?.changeToLogInBtn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding?.signUpBtn?.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val name = binding?.nameET?.text.toString().trim{ it <= ' '}
        val email = binding?.emailET?.text.toString().trim{ it <= ' '}
        val password = binding?.passwordET?.text.toString()

        if (validateForm(name, email, password)) {
            showProgressDialog()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        Firestore().registerUser(this, user)
                    } else {
                        hideProgressDialog()
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            showErrorSnackBar("Please create a strong password")
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            showErrorSnackBar("Email address is badly formatted")
                        } catch (e: FirebaseAuthUserCollisionException) {
                            showErrorSnackBar("This user already exist")
                        } catch (e: Exception) {
                            showErrorSnackBar(e.message.toString())
                        }
                    }
                }
        } else {
            showErrorSnackBar(resources.getString(R.string.please_fill_all_the_required_fields))
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    fun userRegistered(user: User) {
        hideProgressDialog()
        saveUserDataInSharedPreference(user)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding?.toolBar)
        supportActionBar?.title = ""
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
        }
        binding?.backBtn?.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}