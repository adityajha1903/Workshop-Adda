package com.example.workshopadda.activities

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import com.example.workshopadda.R
import com.example.workshopadda.adapters.ViewPagerAdapter
import com.example.workshopadda.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpBackSlide()

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding?.viewPager?.adapter = adapter
        TabLayoutMediator(binding?.tab!!, binding?.viewPager!!) { tab, position ->
            when (position) {
                0 -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_space_dashboard_24, null)
                1 -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_format_list_bulleted_24, null)
                else -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_person_24, null)
            }
        }.attach()
    }

    private fun setUpBackSlide() {
        setSupportActionBar(binding?.toolBar)
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