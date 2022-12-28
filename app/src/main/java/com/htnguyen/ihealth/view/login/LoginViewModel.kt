package com.htnguyen.ihealth.view.login

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel

class LoginViewModel : BaseViewModel() {
    val idAccount: MutableLiveData<String> = MutableLiveData(null)
    val password: MutableLiveData<String> = MutableLiveData(null)
    val confirmPassword: MutableLiveData<String> = MutableLiveData(null)
    val validationEmailMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationPhoneMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationConfirmPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationMatchPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
}