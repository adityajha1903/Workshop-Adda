package com.example.workshopadda.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.workshopadda.adapters.WorkshopItemRecyclerAdapter
import com.example.workshopadda.databinding.FragmentDashboardBinding
import com.example.workshopadda.db.WorkshopDatabase
import com.example.workshopadda.models.Workshop
import com.example.workshopadda.utils.Constants
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var binding: FragmentDashboardBinding? = null

    override fun onResume() {
        getAvailableWorkshops()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        return binding?.root
    }

    private fun getAvailableWorkshops() {
        val workshopDb = WorkshopDatabase.getInstance(context?.applicationContext!!)
        val workshopDao = workshopDb.workshopDao()
        lifecycleScope.launch {
            val allWorkshopList = ArrayList(workshopDao.fetchAllPlaces())
            val appliedWorkshopList = ArrayList<Workshop>()
            val sharedPrefs = activity?.getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES, MODE_PRIVATE)
            val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")
            assignedWorkshopsIdsString?.forEach { char ->
                allWorkshopList.forEach { workshop ->
                    if (char.toString().toInt() == workshop.id) {
                        appliedWorkshopList.add(workshop)
                    }
                }
            }
            if (appliedWorkshopList.isEmpty()) {
                binding?.warningTV?.visibility = View.VISIBLE
            } else {
                binding?.warningTV?.visibility = View.GONE
                setUpRecyclerView(allWorkshopList, appliedWorkshopList)
            }
        }
    }

    private fun setUpRecyclerView(allWorkshopList: ArrayList<Workshop>, appliedWorkshopList: ArrayList<Workshop>) {
        binding?.dashboardRV?.adapter = WorkshopItemRecyclerAdapter(context?.applicationContext!!, allWorkshopList, appliedWorkshopList, true) {}
        binding?.dashboardRV?.layoutManager = LinearLayoutManager(context)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}