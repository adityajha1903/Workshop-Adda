package com.adiandrodev.workshopadda.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandrodev.workshopadda.ui.activity.LogInActivity
import com.adiandrodev.workshopadda.ui.activity.MainActivity
import com.adiandrodev.workshopadda.ui.adapter.WorkshopItemRecyclerAdapter
import com.adiandrodev.workshopadda.databinding.FragmentWorkshopsBinding
import com.adiandrodev.workshopadda.data.db.WorkshopDatabase
import com.adiandrodev.workshopadda.data.firebase.FirestoreRepository
import com.adiandrodev.workshopadda.util.Constants
import com.adiandrodev.workshopadda.data.model.User
import com.adiandrodev.workshopadda.data.model.Workshop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkshopsFragment : Fragment() {

    private var binding: FragmentWorkshopsBinding? = null

    private var allWorkshopList = ArrayList<Workshop>()
    private var appliedWorkshopList = ArrayList<Workshop>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkshopsBinding.inflate(layoutInflater)

        setUpRecyclerView()

        getData()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        val userId = (requireActivity() as MainActivity).getCurrentUserId()
        binding?.workshopRv?.layoutManager = LinearLayoutManager(context)
        binding?.workshopRv?.adapter = WorkshopItemRecyclerAdapter(context?.applicationContext!!, allWorkshopList, ArrayList(),false, { id ->
            if (userId.isNotEmpty()) {
                (requireActivity() as MainActivity).changesMadeForDashboard = true
                (requireActivity() as MainActivity).changesMadeForProfile = true
                if (Constants.isInternetAvailable(context?.applicationContext!!)) {
                    updateUser(id, true)
                } else {
                    (activity as MainActivity).showErrorSnackBar("Network not available")
                }
            } else {
                startActivity(Intent(context, LogInActivity::class.java))
            }
        },{ id ->
            if (userId.isNotEmpty()) {
                (requireActivity() as MainActivity).changesMadeForDashboard = true
                (requireActivity() as MainActivity).changesMadeForProfile = true
                if (Constants.isInternetAvailable(context?.applicationContext!!)) {
                    updateUser(id, false)
                } else {
                    (activity as MainActivity).showErrorSnackBar("Network not available")
                }
            } else {
                startActivity(Intent(context, LogInActivity::class.java))
            }
        })
    }

    private fun getData() {
        val workshopDb = WorkshopDatabase.getInstance(context?.applicationContext!!)
        val workshopDao = workshopDb.workshopDao()
        lifecycleScope.launch(Dispatchers.IO) {
            allWorkshopList = ArrayList(workshopDao.fetchAllWorkshop())
            val sharedPrefs = activity?.getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES, Context.MODE_PRIVATE)
            val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")?:""
            appliedWorkshopList = assignedWorkshopsIdsString.getAppliedWorkshops(allWorkshopList)
            sharedPrefs?.edit()?.putInt(Constants.NO_OF_APPLIED_WORKSHOP, appliedWorkshopList.size)?.apply()
            withContext(Dispatchers.Main) {
                (binding?.workshopRv?.adapter as WorkshopItemRecyclerAdapter).dataSetChanged(allWorkshopList, appliedWorkshopList)
            }
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

    private fun updateUser(appliedWorkshopId: Int, applied: Boolean) {
        val userHashMap = HashMap<String, Any>()
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.WORKSHOP_ADDA_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val id = sharedPrefs?.getString(Constants.USER_ID, "")
        val name = sharedPrefs?.getString(Constants.USER_NAME, "")
        val email = sharedPrefs?.getString(Constants.USER_EMAIL, "")
        val assignedWorkshopsIdsString = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")?:""
        userHashMap[Constants.USER_ID] = id?:""
        userHashMap[Constants.USER_NAME] = name?:""
        userHashMap[Constants.USER_EMAIL] = email?:""
        val newAssignedWorkshopsIdsString = if (applied) {
            "$assignedWorkshopsIdsString$appliedWorkshopId+"
        } else {
            assignedWorkshopsIdsString.replaceId(appliedWorkshopId)
        }
        userHashMap[Constants.ASSIGNED_WORKSHOPS_IDS_STRING] = newAssignedWorkshopsIdsString
        (activity as MainActivity).showProgressDialog()
        (requireActivity() as MainActivity).updateUserData(userHashMap) {  isSuccess, workshopsIdsString ->
            if (isSuccess && workshopsIdsString != null) {
                appliedWorkshopList = workshopsIdsString.getAppliedWorkshops(allWorkshopList)
                (binding?.workshopRv?.adapter as WorkshopItemRecyclerAdapter).dataSetChanged(allWorkshopList, appliedWorkshopList)
                (requireActivity() as MainActivity).loadUserData { isSuccess2, user, _ ->
                    if (isSuccess2 && user != null) {
                        saveUserDataInSharedPreference(user)
                        (activity as MainActivity).hideProgressDialog()
                    } else {
                        (activity as MainActivity).hideProgressDialog()
                    }
                }
            } else {
                (activity as MainActivity).hideProgressDialog()
            }
        }
    }

    private fun String.replaceId(id: Int): String {
        var newStr = ""
        newStr = if (this == "$id+") {
            return newStr
        } else if (this.startsWith(id.toString())) {
            var i = 0
            while (this[i] != '+') {
                i++
            }
            i++
            this.substring(i, this.length)
        } else {
            this.replace("+$id", "")
        }
        return newStr
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
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}