package com.example.workshopadda.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workshopadda.activities.LogInActivity
import com.example.workshopadda.activities.MainActivity
import com.example.workshopadda.adapters.WorkshopItemRecyclerAdapter
import com.example.workshopadda.databinding.FragmentWorkshopsBinding
import com.example.workshopadda.db.WorkshopDatabase
import com.example.workshopadda.firebase.Firestore
import com.example.workshopadda.models.User
import com.example.workshopadda.models.Workshop
import com.example.workshopadda.utils.Constants
import kotlinx.coroutines.launch

class WorkshopsFragment : Fragment() {

    private var binding: FragmentWorkshopsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkshopsBinding.inflate(layoutInflater)

        setUpRecyclerView()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        val workshopDb = WorkshopDatabase.getInstance(context?.applicationContext!!)
        val workshopDao = workshopDb.workshopDao()
        lifecycleScope.launch {
            val allWorkshopList = ArrayList(workshopDao.fetchAllPlaces())
            val appliedWorkshopList = ArrayList<Workshop>()
            val sharedPrefs = activity?.getSharedPreferences(
                Constants.WORKSHOP_ADDA_PREFERENCES,
                Context.MODE_PRIVATE
            )
            val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")
            assignedWorkshopsIdsString?.forEach { char ->
                allWorkshopList.forEach { workshop ->
                    if (char.toString().toInt() == workshop.id) {
                        appliedWorkshopList.add(workshop)
                    }
                }
            }
            Log.d(Constants.WORKSHOP_ADDA_PREFERENCES, "allWorkshops: ${allWorkshopList.size}, appliedWorkshops: ${appliedWorkshopList.size}")
            binding?.workshopRv?.layoutManager = LinearLayoutManager(context)
            binding?.workshopRv?.adapter = WorkshopItemRecyclerAdapter(context?.applicationContext!!, allWorkshopList, appliedWorkshopList, false) { id ->
                if (Firestore().getCurrentUserId().isNotEmpty()) {
                    if (Constants.isInternetAvailable(context?.applicationContext!!)) {
                        updateUser(id)
                    } else {
                        (activity as MainActivity).showErrorSnackBar("Network not available")
                    }
                } else {
                    startActivity(Intent(context, LogInActivity::class.java))
                }
            }
        }
    }

    private fun updateUser(appliedWorkshopId: Int) {
        val userHashMap = HashMap<String, Any>()
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.WORKSHOP_ADDA_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val id = sharedPrefs?.getString(Constants.USER_ID, "")
        val name = sharedPrefs?.getString(Constants.USER_NAME, "")
        val email = sharedPrefs?.getString(Constants.USER_EMAIL, "")
        val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")
        userHashMap[Constants.USER_ID] = id!!
        userHashMap[Constants.USER_NAME] = name!!
        userHashMap[Constants.USER_EMAIL] = email!!

        val newAssignedWorkshopsIdsString = assignedWorkshopsIdsString + appliedWorkshopId.toString()
        userHashMap[Constants.ASSIGNED_WORKSHOPS_IDS_STRING] = newAssignedWorkshopsIdsString

        (activity as MainActivity).showProgressDialog()
        Firestore().updateUserData(this, userHashMap)
    }

    fun userUpdated() {
        Firestore().loadUserDataFromFragment(this)
    }

    fun userLoaded(user: User) {
        saveUserDataInSharedPreference(user)
        (activity as MainActivity).hideProgressDialog()
    }

    private fun saveUserDataInSharedPreference(user: User) {
        val sharedPref = activity?.getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPref?.edit()
        editor?.putString(Constants.USER_ID, user.id)
        editor?.putString(Constants.USER_NAME, user.name)
        editor?.putString(Constants.USER_EMAIL, user.email)
        editor?.putString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, user.assignedWorkshopsIdsString)
        editor?.apply()
        setUpRecyclerView()
    }

    fun userUpdateFailed() {
        (activity as MainActivity).hideProgressDialog()
    }

    fun userLoadingFailed() {
        (activity as MainActivity).hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}