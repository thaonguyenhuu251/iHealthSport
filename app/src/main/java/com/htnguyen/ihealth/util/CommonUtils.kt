package com.htnguyen.ihealth.util

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
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(activityDaily)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateHealthDaily() {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("health_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(ActivityDaily())
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateHeartBeat(heartBeat: Float) {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("health_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .child("heartBeat")
            .setValue(heartBeat)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateFelling(fellingToday: Int) {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("health_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .child("fellingToday")
            .setValue(fellingToday)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}