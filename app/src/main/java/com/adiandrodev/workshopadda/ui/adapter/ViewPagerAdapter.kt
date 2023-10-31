package com.adiandrodev.workshopadda.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adiandrodev.workshopadda.ui.fragment.DashboardFragment
import com.adiandrodev.workshopadda.ui.fragment.ProfileFragment
import com.adiandrodev.workshopadda.ui.fragment.WorkshopsFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> DashboardFragment()
            1 -> WorkshopsFragment()
            else -> ProfileFragment()
        }
    }

}