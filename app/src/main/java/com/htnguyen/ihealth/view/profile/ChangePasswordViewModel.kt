package com.htnguyen.ihealth.view.profile

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel

class ChangePasswordViewModel : BaseViewModel() {
    val oldPassword: MutableLiveData<String> = MutableLiveData(null)
    val newPassword: MutableLiveData<String> = MutableLiveData(null)
    val confirmPassword: MutableLiveData<String> = MutableLiveData(null)
    val validationOldPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationNewPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationConfirmPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationMatchPasswordMsg: MutableLiveData<String> = MutableLiveData(null)

    fun isOldPasswordEmpty() : Boolean {
        return if (oldPassword.value != null && oldPassword.value.toString().trim().isNotEmpty()) {
            validationOldPasswordMsg.value = null
            true
        } else {
            validationOldPasswordMsg.value = "cannot be left blank"
            false
        }
    }

    fun isPasswordNotEmpty() : Boolean {
        return newPassword.value.toString().trim().isNotEmpty()
                && confirmPassword.value.toString().trim().isNotEmpty()
                && !newPassword.value.isNullOrBlank()
                && !confirmPassword.value.isNullOrBlank()
    }

    fun isValidatePassword() : Boolean {
        return if (newPassword.value != null && newPassword.value.toString().trim().isNotEmpty()) {
            if (isPasswordValid(newPassword.value.toString())) {
                validationNewPasswordMsg.value = null
                true
            } else {
                validationNewPasswordMsg.value = "Password must contain at least 1 letter, 1 number, 1 uppercase character"
                false
            }
        } else {
            validationNewPasswordMsg.value = "cannot be left blank"
            false
        }
    }

    fun isValidateConfirmPassword() : Boolean {
        return if (confirmPassword.value != null && confirmPassword.value.toString().trim().isNotEmpty()) {
            if (isPasswordValid(confirmPassword.value.toString())) {
                validationConfirmPasswordMsg.value = null
                true
            } else {
                validationConfirmPasswordMsg.value = "Password must contain at least 1 letter, 1 number, 1 uppercase character"
                false
            }
        } else {
            validationConfirmPasswordMsg.value = "cannot be left blank"
            false
        }
    }

    fun isValidatePasswordMatch() : Boolean {
        return if (isPasswordNotEmpty()) {
            if (newPassword.value.toString().trim() == confirmPassword.value.toString().trim()) {
                validationMatchPasswordMsg.value = null
                true
            } else {
                validationMatchPasswordMsg.value = "Password not match"
                false
            }
        } else {
            false
        }
    }

    fun isValidatePasswordFinish() : Boolean {
        return isValidatePasswordMatch()
                && isValidatePassword() && isValidateConfirmPassword()
    }

    private fun isPasswordValid(password: String): Boolean {
        return Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{8,}\$").matches(password)
    }
}