package com.htnguyen.ihealth.view.profile

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.util.PreferencesUtil

class ProfileViewModel : BaseViewModel() {
    val birthDay: MutableLiveData<String> = MutableLiveData(null)
    val birthDayLong: MutableLiveData<Long> = MutableLiveData(PreferencesUtil.userBirthDay)
    val gender: MutableLiveData<Boolean> = MutableLiveData(PreferencesUtil.userGender)
    val progressWeight: MutableLiveData<Float> = MutableLiveData(PreferencesUtil.userWeight)
    val progressHeight: MutableLiveData<Float> = MutableLiveData(PreferencesUtil.userHeight)

    val achieveStep: MutableLiveData<String> = MutableLiveData(null)

    val achieveTime: MutableLiveData<String> = MutableLiveData(null)
    val achieveCalo: MutableLiveData<String> = MutableLiveData(null)
    val achieveDistance: MutableLiveData<String> = MutableLiveData(null)
    val achieveMoutain: MutableLiveData<String> = MutableLiveData(null)
}