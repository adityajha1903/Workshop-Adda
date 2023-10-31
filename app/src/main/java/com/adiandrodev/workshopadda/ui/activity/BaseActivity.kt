package com.adiandrodev.workshopadda.ui.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.adiandrodev.workshopadda.R
import com.adiandrodev.workshopadda.databinding.ProgressDialogLayoutBinding
import com.adiandrodev.workshopadda.util.Constants
import com.adiandrodev.workshopadda.data.model.User
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    private lateinit var mDialogBinding: ProgressDialogLayoutBinding

    fun showProgressDialog() {
        mProgressDialog = Dialog(this)
        mDialogBinding = ProgressDialogLayoutBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(mDialogBinding.root)
        mDialogBinding.txtTv.text = resources.getString(R.string.please_wait)
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    fun showErrorSnackBar(message: String) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        snackBar.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        snackBar.show()
    }

    fun saveUserDataInSharedPreference(user: User) {
        val sharedPref = getSharedPreferences(Constants.WORKSHOP_ADDA_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constants.USER_ID, user.id)
        editor.putString(Constants.USER_NAME, user.name)
        editor.putString(Constants.USER_EMAIL, user.email)
        editor.putString(Constants.ASSIGNED_WORKSHOPS_IDS_STRING, user.assignedWorkshopsIdsString)
        editor.apply()
    }
}