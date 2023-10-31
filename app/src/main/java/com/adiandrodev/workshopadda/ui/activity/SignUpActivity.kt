package com.adiandrodev.workshopadda.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.adiandrodev.workshopadda.R
import com.adiandrodev.workshopadda.data.firebase.AuthRepository
import com.adiandrodev.workshopadda.databinding.ActivitySignUpBinding
import com.adiandrodev.workshopadda.ui.viewmodel.SignUpViewModel
import com.adiandrodev.workshopadda.ui.viewmodel.SignUpViewModelFactory

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null

    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpToolBar()

        initViewModel()

        binding?.changeToLogInBtn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding?.signUpBtn?.setOnClickListener {
            registerUser()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(AuthRepository()))[SignUpViewModel::class.java]
    }

    private fun registerUser() {
        val name = binding?.nameET?.text.toString().trim{ it <= ' '}
        val email = binding?.emailET?.text.toString().trim{ it <= ' '}
        val password = binding?.passwordET?.text.toString()

        if (validateForm(name, email, password)) {
            showProgressDialog()
            viewModel.registerUser(name, email, password) { isSuccess, user, error ->
                if (isSuccess && user != null) {
                    saveUserDataInSharedPreference(user)
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    hideProgressDialog()
                    startActivity(i)
                    finish()
                } else {
                    if (error != null) {
                        showErrorSnackBar(error)
                    }
                    hideProgressDialog()
                }
            }
        } else {
            showErrorSnackBar(resources.getString(R.string.please_fill_all_the_required_fields))
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
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