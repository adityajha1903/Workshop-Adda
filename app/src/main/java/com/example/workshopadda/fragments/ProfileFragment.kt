package com.example.workshopadda.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.workshopadda.activities.IntroActivity
import com.example.workshopadda.activities.MainActivity
import com.example.workshopadda.databinding.FragmentProfileBinding
import com.example.workshopadda.firebase.Firestore
import com.example.workshopadda.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding?.signOutBtn?.setOnClickListener {
            if (Firestore().getCurrentUserId().isEmpty()) {
                (activity as MainActivity).showErrorSnackBar("You are not logged in.")
            } else {
                showAlertDialogForSignOut()
            }
        }

        return binding?.root
    }

    private fun showAlertDialogForSignOut() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to sign out?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            FirebaseAuth.getInstance().signOut()
            activity?.getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES, MODE_PRIVATE)?.edit()?.clear()?.apply()
            val i = Intent(activity, IntroActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            dialog.dismiss()
            startActivity(i)
            activity?.finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onResume() {
        updateUserProfile()
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUserProfile() {
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.WORKSHOP_ADDA_PREFERENCES,
            MODE_PRIVATE
        )
        val name = sharedPrefs?.getString(Constants.USER_NAME, "")
        val email = sharedPrefs?.getString(Constants.USER_EMAIL, "")
        val noOfAppliedWorkshops = sharedPrefs?.getString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, "")?.length

        if (name?.isNotEmpty() == true) {
            binding?.userNameTVProfileActivity?.text = name
            binding?.firstLetterTVProfileActivity?.text = name[0].uppercase()
        } else {
            binding?.userNameTVProfileActivity?.text = "Guest"
            binding?.firstLetterTVProfileActivity?.text = "G"
        }

        if (email?.isNotEmpty() == true) {
            binding?.userEmailTVProfileActivity?.text = email
        } else {
            binding?.userEmailTVProfileActivity?.text = "***********@gmail.com"
        }
        binding?.userAppliedWorkshops?.text = noOfAppliedWorkshops.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}