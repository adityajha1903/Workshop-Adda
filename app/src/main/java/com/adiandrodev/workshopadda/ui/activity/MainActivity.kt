package com.adiandrodev.workshopadda.ui.activity

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.adiandrodev.workshopadda.R
import com.adiandrodev.workshopadda.data.db.WorkshopDatabase
import com.adiandrodev.workshopadda.data.db.WorkshopRepository
import com.adiandrodev.workshopadda.data.firebase.AuthRepository
import com.adiandrodev.workshopadda.data.firebase.FirestoreRepository
import com.adiandrodev.workshopadda.data.model.User
import com.adiandrodev.workshopadda.ui.adapter.ViewPagerAdapter
import com.adiandrodev.workshopadda.databinding.ActivityMainBinding
import com.adiandrodev.workshopadda.ui.viewmodel.MainViewModel
import com.adiandrodev.workshopadda.ui.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpBackSlide()

        initViewModel()

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

    private fun initViewModel() {
        val workshopDao = WorkshopDatabase.getInstance(applicationContext).workshopDao()
        val workshopRepository = WorkshopRepository(workshopDao)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(workshopRepository, FirestoreRepository(), AuthRepository())
        )[MainViewModel::class.java]
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

    fun getCurrentUserId(): String {
        return viewModel.getCurrentUserId()
    }

    fun updateUserData(
        userHashMap: HashMap<String, Any>,
        userUpdateResult: (isSuccess: Boolean, assignedWorkshopsIdsString: String?) -> Unit
    ) {
        viewModel.updateUserData(userHashMap, userUpdateResult)
    }

    fun loadUserData(
        userLoadingResult: (isSuccess: Boolean, user: User?, error: String?) -> Unit
    ) {
        viewModel.loadUserData(userLoadingResult)
    }

    fun getAllWorkshops() = viewModel.getAllWorkshops()

    fun signOut() {
        viewModel.signOut()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    var changesMadeForDashboard = true
    var changesMadeForProfile = true
}