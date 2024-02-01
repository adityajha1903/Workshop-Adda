package com.adiandrodev.workshopadda.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.adiandrodev.workshopadda.data.firebase.FirestoreRepository
import com.adiandrodev.workshopadda.ui.adapter.IntroViewPagerAdapter
import com.adiandrodev.workshopadda.databinding.ActivityIntroBinding
import com.google.android.material.tabs.TabLayoutMediator

class IntroActivity : AppCompatActivity() {

    private var binding: ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpBackSlide()

        binding?.introSliderViewPager?.adapter = IntroViewPagerAdapter(applicationContext)
        TabLayoutMediator(binding?.introSliderTabLayout!!, binding?.introSliderViewPager!!) { _, _ -> }.attach()

        binding?.logInBtn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding?.signUpBtn?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding?.guestBtn?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onStart() {
        val userId = FirestoreRepository().getCurrentUserId()
        if (userId.isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onStart()
    }

    private fun setUpBackSlide() {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}