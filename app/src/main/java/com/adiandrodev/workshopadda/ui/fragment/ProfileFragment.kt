package com.adiandrodev.workshopadda.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.adiandrodev.workshopadda.ui.activity.IntroActivity
import com.adiandrodev.workshopadda.ui.activity.MainActivity
import com.adiandrodev.workshopadda.databinding.FragmentProfileBinding
import com.adiandrodev.workshopadda.util.Constants

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding?.signOutBtn?.setOnClickListener {
            if ((requireActivity() as MainActivity).getCurrentUserId().isEmpty()) {
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
            (requireActivity() as MainActivity).signOut()
            activity?.getSharedPreferences(
                Constants.WORKSHOP_ADDA_PREFERENCES,
                Context.MODE_PRIVATE
            )?.edit()?.clear()?.apply()
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

    private var firstRun = true

    override fun onResume() {
        if (firstRun || (requireActivity() as MainActivity).changesMadeForProfile) {
            updateUserProfile()
        }
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUserProfile() {
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.WORKSHOP_ADDA_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val name = sharedPrefs?.getString(Constants.USER_NAME, "")
        val email = sharedPrefs?.getString(Constants.USER_EMAIL, "")
        val noOfAppliedWorkshops = sharedPrefs?.getInt(Constants.NO_OF_APPLIED_WORKSHOP, 0)?: 0

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