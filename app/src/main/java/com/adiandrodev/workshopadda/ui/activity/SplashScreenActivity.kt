package com.adiandrodev.workshopadda.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adiandrodev.workshopadda.databinding.ActivitySplashScreenBinding
import com.adiandrodev.workshopadda.data.firebase.FirestoreRepository

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val userId = FirestoreRepository().getCurrentUserId()
            if (userId.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}