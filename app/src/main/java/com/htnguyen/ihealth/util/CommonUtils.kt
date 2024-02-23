package com.htnguyen.ihealth.util

import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.model.ActivityDaily
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.dateInMillis

object CommonUtils {
    fun getCalories(steps: Int): String {
        val Cal = (steps * 0.045).toInt()
        return "$Cal calories /"
    }

    fun getCaloriesInt(steps: Int): Int {
        val Calories = (steps * 0.045).toInt()
        return Calories
    }

    fun getDistanceCovered(steps: Int): String {
        val feet = (steps * 2.5).toInt()
        val distance = (feet/3.281).toInt()
        //val finalDistance: Float = String.format("%.2f", distance).toFloat()
        return "$distance meter"
    }
    fun getDistanceInt(steps: Int): Int {
        val feet = (steps * 2.5).toInt()
        val distance = (feet/3.281).toInt()
        return distance
    }


    fun updateActivityDaily(steps: Int, followStep: Int) {
        val activityDaily = ActivityDaily(
            step = steps,
            followStep = followStep,
            timeActive = getDistanceInt(steps).toFloat(),
            calo = getCaloriesInt(steps).toFloat()
        )

        FirebaseUtils.activityDaily
            .child("RecordHistory")
            .child(PreferencesUtil.idPrivate.toString())
            .child("ActivityDaily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(activityDaily)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateHealthDaily() {
        FirebaseUtils.activityDaily
            .child("RecordHistory")
            .child(PreferencesUtil.idPrivate.toString())
            .child("HealthDaily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(ActivityDaily())
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateHeartBeat(heartBeat: Float) {
        FirebaseUtils.activityDaily
            .child("RecordHistory")
            .child(PreferencesUtil.idPrivate.toString())
            .child("HealthDaily")
            .child("date" + Calendar().dateInMillis.toString())
            .child("heartBeat")
            .setValue(heartBeat)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateFelling(fellingToday: Int) {
        FirebaseUtils.activityDaily
            .child("RecordHistory")
            .child(PreferencesUtil.idPrivate.toString())
            .child("HealthDaily")
            .child("date" + Calendar().dateInMillis.toString())
            .child("fellingToday")
            .setValue(fellingToday)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }



    fun getColorString(color: String): String {
        return when (color) {
            Constant.COLOR_RED -> "Red"
            Constant.COLOR_PINK -> "Pink"
            Constant.COLOR_DARKPINK -> "Dark pink"
            Constant.COLOR_VIOLET -> "Violet"
            Constant.COLOR_BLUE -> "Blue"
            Constant.COLOR_SKYBLUE -> "Sky blue"
            Constant.COLOR_GREEN -> "Green"
            Constant.COLOR_GREY -> "Grey"
            Constant.COLOR_BROWN -> "Brown"
            else -> "Blue"
        }
    }

    fun getStringColor(color: String): String {
        return when (color) {
            "Red" -> Constant.COLOR_RED
            "Pink" -> Constant.COLOR_PINK
            "Dark pink" -> Constant.COLOR_DARKPINK
            "Violet" -> Constant.COLOR_VIOLET
            "Blue" -> Constant.COLOR_BLUE
            "Sky blue" -> Constant.COLOR_SKYBLUE
            "Green" -> Constant.COLOR_GREEN
            "Grey" -> Constant.COLOR_GREY
            "Brown" -> Constant.COLOR_BROWN
            else -> Constant.COLOR_BLUE
        }
    }

    fun getStringLanguage(language: String): String {
        return when (language) {
            Constant.LG_VIETNAMESE -> "Vietnamese"
            Constant.LG_ENGLISH -> "English"
            Constant.LG_RUSSIAN -> "Russian"
            Constant.LG_KOREAN -> "Korean"
            Constant.LG_LAOS -> "Laos"
            Constant.LG_THAI -> "Thai"
            Constant.LG_MYANMAR -> "Myanmar"
            Constant.LG_CHINESE -> "Chinese"
            Constant.LG_JAPANESE -> "Japanese"
            Constant.LG_FILIPINO -> "Filipino"
            Constant.LG_INDONESIAN -> "Indonesian"
            Constant.LG_SPANISH -> "Spanish"
            Constant.LG_FRENCH -> "French"
            Constant.LG_INDIAN -> "Indian"
            Constant.LG_GERMAN -> "German"
            Constant.LG_ITALIAN -> "Italian"
            else -> "Vietnamese"
        }
    }

    fun getLanguages(language: String): Int {
        return when (language) {
            Constant.LG_VIETNAMESE -> R.drawable.ic_language_vietnam
            Constant.LG_ENGLISH -> R.drawable.ic_language_english_uk
            Constant.LG_RUSSIAN -> R.drawable.ic_language_russian
            Constant.LG_KOREAN -> R.drawable.ic_language_south_korea
            Constant.LG_LAOS -> R.drawable.ic_language_laos
            Constant.LG_THAI -> R.drawable.ic_language_thailand
            Constant.LG_MYANMAR -> R.drawable.ic_language_myanmar
            Constant.LG_CHINESE -> R.drawable.ic_china_svgrepo_com
            Constant.LG_JAPANESE -> R.drawable.ic_language_japan
            Constant.LG_FILIPINO -> R.drawable.ic_language_philippines
            Constant.LG_INDONESIAN -> R.drawable.ic_language_indonesia
            Constant.LG_SPANISH -> R.drawable.ic_language_spain
            Constant.LG_FRENCH -> R.drawable.ic_language_france
            Constant.LG_INDIAN -> R.drawable.ic_language_india
            Constant.LG_GERMAN -> R.drawable.ic_language_germany
            Constant.LG_ITALIAN -> R.drawable.ic_language_italy
            else -> R.drawable.ic_language_vietnam
        }
    }

    fun formatNumber(number: Int): String {
        var formatNumber = ""
        var result = number
        val listResult = ArrayList<Int>()
        do {
            listResult.add(result % 1000)
            result /= 1000
        } while (result > 0)

        for (i in listResult.size - 1 downTo 0 step 1) {
            if (i == listResult.size - 1) {
                formatNumber += "${listResult[i]}"
            } else {
                if (listResult[i] > 100) {
                    formatNumber += ".${listResult[i]}"
                } else if (listResult[i] > 10) {
                    formatNumber += ".0${listResult[i]}"
                } else {
                    formatNumber += ".00${listResult[i]}"
                }
            }
        }

        return formatNumber
    }
}