package com.adiandrodev.workshopadda.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandrodev.workshopadda.ui.activity.MainActivity
import com.adiandrodev.workshopadda.ui.adapter.WorkshopItemRecyclerAdapter
import com.adiandrodev.workshopadda.databinding.FragmentDashboardBinding
import com.adiandrodev.workshopadda.util.Constants
import com.adiandrodev.workshopadda.data.model.Workshop

class DashboardFragment : Fragment() {

    private var binding: FragmentDashboardBinding? = null

    private var allWorkshopList = ArrayList<Workshop>()
    private var appliedWorkshopList = ArrayList<Workshop>()

    private var firstRun = true

    override fun onResume() {
        if (firstRun) {
            setUpRecyclerView(allWorkshopList, appliedWorkshopList)
            firstRun = false
        }
        if((requireActivity() as MainActivity).changesMadeForDashboard) {
            (requireActivity() as MainActivity).changesMadeForDashboard = false
            getAvailableWorkshops()
        }
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
        (requireActivity() as MainActivity).getAllWorkshops().observe(requireActivity()) {
            allWorkshopList = ArrayList(it)
            val sharedPrefs = activity?.getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES, Context.MODE_PRIVATE)
            val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "") ?: ""
            appliedWorkshopList = assignedWorkshopsIdsString.getAppliedWorkshops(allWorkshopList)
            if (appliedWorkshopList.isEmpty()) {
                binding?.warningTV?.visibility = View.VISIBLE
            } else {
                binding?.warningTV?.visibility = View.GONE
            }
            (binding?.dashboardRV?.adapter as WorkshopItemRecyclerAdapter).dataSetChanged(allWorkshopList, appliedWorkshopList)
        }
    }

    private fun String.getAppliedWorkshops(allWorkshopList: ArrayList<Workshop>): ArrayList<Workshop> {
        val appliedWorkshop = ArrayList<Workshop>()
        var start = 0
        var end = 0
        while (start < this.length && end < this.length) {
            if (this[end] == '+') {
                appliedWorkshop.add(allWorkshopList.getWithId(this.substring(start, end).toInt()))
                end++
                start = end
            } else {
                end++
            }
        }
        return appliedWorkshop
    }

    private fun ArrayList<Workshop>.getWithId(id: Int): Workshop {
        var workshop = Workshop()
        this.forEach {
            if (it.id == id) {
                workshop = it
            }
        }
        return workshop
    }

    private fun setUpRecyclerView(
        allWorkshopList: ArrayList<Workshop>,
        appliedWorkshopList: ArrayList<Workshop>
    ) {
        binding?.dashboardRV?.adapter = WorkshopItemRecyclerAdapter(
            requireActivity(),
            allWorkshopList, appliedWorkshopList,
            true, {}, {}
        )
        binding?.dashboardRV?.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}