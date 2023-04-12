package com.example.workshopadda.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.workshopadda.databinding.ActivitySplashScreenBinding
import com.example.workshopadda.firebase.Firestore

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val intent = Intent(this, IntroActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            val userId = Firestore().getCurrentUserId()
            if (userId.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(intent)
            }
            finish()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}