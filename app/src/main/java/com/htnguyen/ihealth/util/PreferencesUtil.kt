package com.htnguyen.ihealth.util

import android.content.Context
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.view.IHealthApplication

object PreferencesUtil {
    private val preferences = IHealthApplication.mInstance
        .getSharedPreferences(
            IHealthApplication.mInstance.getString(R.string.pref_name_ihealth),
            Context.MODE_PRIVATE)
    private val preferencesEdit = preferences.edit()

    // user
    const val PREF_AGREE_TERMS = "PREF_AGREE_TERMS"
    const val PREF_USER = "PREF_USER"
    const val PREF_COOKIE = "PREF_COOKIE"
    const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
    const val PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN"
    const val PREF_NOTIFICATION = "PREF_NOTIFICATION"
    const val PREF_NOTIFICATION_COUNT = "PREF_NOTIFICATION_COUNT"

    const val PREF_USER_ID = "PREF_USER_ID"
    const val PREF_USER_PASSWORD = "PREF_USER_PASSWORD"

    fun deleteCache() {
        preferencesEdit.clear().commit()
    }

    var cookies: HashSet<String?>
        get() {
            return preferences.getStringSet(PREF_COOKIE, setOf())!!
                .map { it }.toHashSet()
        }
        set(value) {
            preferencesEdit.putStringSet(PREF_COOKIE, value.map { it }.toHashSet()).apply()
        }

    fun removeValue(key: String) {
        preferencesEdit.remove(key)
    }

    var isAgreedTerms: Boolean
        get() {
            return preferences.getBoolean(PREF_AGREE_TERMS, false)
        }
        set(value) {
            preferencesEdit.putBoolean(PREF_AGREE_TERMS, value).apply()
        }

    var accessToken: String?
        get() {
            return preferences.getString(PREF_ACCESS_TOKEN, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String?
        get() {
            return preferences.getString(PREF_REFRESH_TOKEN, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_REFRESH_TOKEN, value).apply()
        }

    var isNotification: Boolean
        get() {
            return preferences.getBoolean(PREF_NOTIFICATION, false)
        }
        set(value) {
            preferencesEdit.putBoolean(PREF_NOTIFICATION, value).apply()
        }

    var isAnswerNotification: Boolean
        get() {
            return preferences.getBoolean(PREF_NOTIFICATION_COUNT, false)
        }
        set(value) {
            preferencesEdit.putBoolean(PREF_NOTIFICATION_COUNT, value).apply()
        }

    var idUser: String?
        get() {
            return preferences.getString(PREF_USER_ID, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_ID, value).apply()
        }

    var passWord: String?
        get() {
            return preferences.getString(PREF_USER_PASSWORD, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_PASSWORD, value).apply()
        }
}