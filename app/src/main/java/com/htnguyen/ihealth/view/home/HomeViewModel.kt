package com.htnguyen.ihealth.view.home

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.dateInMillis
import com.htnguyen.ihealth.support.hour
import com.htnguyen.ihealth.util.PreferencesUtil

class HomeViewModel : BaseViewModel() {
    val onTime: MutableLiveData<String> = MutableLiveData(timeOnDay())
    val today: MutableLiveData<Long> = MutableLiveData(Calendar().dateInMillis)
    val todayString: MutableLiveData<String> = MutableLiveData(null)

    val kcal: MutableLiveData<Float> = MutableLiveData(0f)
    val followKcal: MutableLiveData<Float> = MutableLiveData(0f)
    val water: MutableLiveData<Int> = MutableLiveData(0)
    val followStep: MutableLiveData<Int> = MutableLiveData(PreferencesUtil.followStep)
    val followWater: MutableLiveData<Int> = MutableLiveData(5)
    val waterMil: MutableLiveData<Int> = MutableLiveData(0)
    val heartBeat: MutableLiveData<Float> = MutableLiveData(0f)
    val fellingToday: MutableLiveData<Int> = MutableLiveData(0)
    val progressWeight: MutableLiveData<Float> = MutableLiveData(PreferencesUtil.userWeight)

    val step: MutableLiveData<Int> = MutableLiveData(0)
    val calories: MutableLiveData<Int> = MutableLiveData(0)
    val meter: MutableLiveData<Int> = MutableLiveData(0)
    
    val stepString: MutableLiveData<String> = MutableLiveData(null)

    private fun timeOnDay() : String {
        val time = Calendar().hour
        return if (time in 0..11) {
            "Good morning"
        } else if (time < 18) {
            "Good afternoon"
        } else {
            "Good evening"
        }
    }
}