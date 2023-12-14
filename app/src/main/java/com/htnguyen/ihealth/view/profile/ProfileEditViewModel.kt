package com.htnguyen.ihealth.view.profile

import androidx.databinding.InverseMethod
import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar

class ProfileEditViewModel : BaseViewModel() {
    val birthDay: MutableLiveData<String> = MutableLiveData(null)
    val birthDayLong: MutableLiveData<Long> = MutableLiveData(null)
    val gender: MutableLiveData<Boolean> = MutableLiveData(null)
    val progressWeight: MutableLiveData<Int> = MutableLiveData(null)
    val progressHeight: MutableLiveData<Int> = MutableLiveData(null)

    val validationNameMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationBirthDayMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationGenderMsg: MutableLiveData<String> = MutableLiveData(null)

    @InverseMethod("toInt")
    fun toString(value: Int): String = "$value"

    fun toInt(value: String): Int = value.toInt() ?: 0

}