package com.htnguyen.ihealth.view.profile

import androidx.databinding.InverseMethod
import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar

class ProfileEditViewModel : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData(null)
    val birthDay: MutableLiveData<String> = MutableLiveData(null)
    val birthDayLong: MutableLiveData<Long> = MutableLiveData(Calendar().timeInMillis)
    val gender: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressWeight: MutableLiveData<Int> = MutableLiveData(60)
    val progressHeight: MutableLiveData<Int> = MutableLiveData(180)

    val validationNameMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationBirthDayMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationGenderMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationConfirmPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationMatchPasswordMsg: MutableLiveData<String> = MutableLiveData(null)

    @InverseMethod("toInt")
    fun toString(value: Int): String = "$value"

    fun toInt(value: String): Int = value.toInt() ?: 0
}