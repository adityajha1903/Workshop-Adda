package com.adiandrodev.workshopadda.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.res.ResourcesCompat
import com.adiandrodev.workshopadda.R

object Constants {

    const val USERS = "users"
    const val WORKSHOP_ADDA_PREFERENCES = "workshop_adda_preferences"
    const val USER_ID = "id"
    const val USER_NAME = "name"
    const val USER_EMAIL = "email"
    const val ASSIGNED_WORKSHOPS_IDS_STRING = "assignedWorkshopsIdsString"
    const val NO_OF_APPLIED_WORKSHOP = "noOFAppliedWorkshop"

    fun getWorkshopImageForId(context: Context, id: Int): Drawable? {
        return when (id) {
            0 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_zero, null)
            1 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_one, null)
            2 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_two, null)
            3 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_three, null)
            4 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_four, null)
            5 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_five, null)
            6 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_six, null)
            7 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_seven, null)
            8 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_eight, null)
            9 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_nine, null)
            10 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_ten, null)
            11 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_eleven, null)
            12 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_twelve, null)
            13 -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_thirteen, null)
            else -> ResourcesCompat.getDrawable(context.resources, R.drawable.workshop_image_id_fourteen, null)
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }
}