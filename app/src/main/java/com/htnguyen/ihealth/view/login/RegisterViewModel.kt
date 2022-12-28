package com.htnguyen.ihealth.view.login

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData(null)
    val phone: MutableLiveData<String> = MutableLiveData(null)
    val password: MutableLiveData<String> = MutableLiveData(null)
    val confirmPassword: MutableLiveData<String> = MutableLiveData(null)
    val validationEmailMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationPhoneMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationConfirmPasswordMsg: MutableLiveData<String> = MutableLiveData(null)
    val validationMatchPasswordMsg: MutableLiveData<String> = MutableLiveData(null)

    fun isEmailEmpty() : Boolean {
        return if (email.value != null && email.value.toString().trim().isNotEmpty()) {
            validationEmailMsg.value = null
            true
        } else {
            validationEmailMsg.value = "cannot be left blank"
            false
        }
    }

    fun isPhoneEmpty() : Boolean {
        return if (phone.value != null && phone.value.toString().trim().isNotEmpty()) {
            validationPhoneMsg.value = null
            true
        } else {
            validationPhoneMsg.value = "cannot be left blank"
            false
        }
    }

    fun isPasswordNotEmpty() : Boolean {
        return password.value.toString().trim().isNotEmpty()
                && confirmPassword.value.toString().trim().isNotEmpty()
                && !password.value.isNullOrBlank()
                && !confirmPassword.value.isNullOrBlank()
    }

    fun isValidatePassword() : Boolean {
        return if (password.value != null && password.value.toString().trim().isNotEmpty()) {
            if (isPasswordValid(password.value.toString())) {
                validationPasswordMsg.value = null
                true
            } else {
                validationPasswordMsg.value = "Password must contain at least 1 letter, 1 number, 1 uppercase character"
                false
            }
        } else {
            validationPasswordMsg.value = "cannot be left blank"
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
            if (password.value.toString().trim() == confirmPassword.value.toString().trim()) {
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