package com.htnguyen.ihealth.util

import android.content.Context
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.view.IHealthApplication
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

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
    const val PREF_DARK_MODE = "PREF_DARK_MODE"

    const val PREF_USER_ID = "PREF_USER_ID"
    const val PREF_USER_ID_PRIVATE = "PREF_USER_ID_PRIVATE"
    const val PREF_USER_PASSWORD = "PREF_USER_PASSWORD"
    const val PREF_USER_NAME = "PREF_USER_NAME"
    const val PREF_USER_BIRTHDAY = "PREF_USER_BIRTHDAY"
    const val PREF_USER_GENDER = "PREF_USER_GENDER"
    const val PREF_USER_HEIGHT = "PREF_USER_HEIGHT"
    const val PREF_USER_WEIGHT = "PREF_USER_WEIGHT"
    const val PREF_USER_PHOTO = "PREF_USER_PHOTO"

    const val PREF_FLOW_STEP = "PREF_FLOW_STEP"
    const val PREF_STEP = "PREF_STEP"

    const val PREF_FOLLOW_WATER = "PREF_FOLLOW_WATER"
    const val PREF_WATER = "PREF_WATER"


    val keyToday: String
        get() {
            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            return simpleDateFormat.format(calendar.time)
        }
    var stepNumber: Int
        get()  {
            return preferences.getInt(keyToday, 0)
        }
        set(value) {
            preferencesEdit.putInt(keyToday, value).apply()
        }

    var followStep: Int
        get()  {
            return preferences.getInt(PREF_FLOW_STEP, 0)
        }
        set(value) {
            preferencesEdit.putInt(PREF_FLOW_STEP, value).apply()
        }

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

    var isDarkMode: Boolean
        get() {
            return preferences.getBoolean(PREF_DARK_MODE, false)
        }
        set(value) {
            preferencesEdit.putBoolean(PREF_DARK_MODE, value).apply()
        }

    var idUser: String?
        get() {
            return preferences.getString(PREF_USER_ID, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_ID, value).apply()
        }

    var idPrivate: String?
        get() {
            return preferences.getString(PREF_USER_ID_PRIVATE, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_ID_PRIVATE, value).apply()
        }

    var passWord: String?
        get() {
            return preferences.getString(PREF_USER_PASSWORD, null)
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_PASSWORD, value).apply()
        }

    var userName: String?
        get() {
            return preferences.getString(PREF_USER_NAME, "")
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_NAME, value).apply()
        }

    var userBirthDay: Long?
        get() {
            return preferences.getLong(PREF_USER_BIRTHDAY, 0L)
        }
        set(value) {
            preferencesEdit.putLong(PREF_USER_BIRTHDAY, value ?: 0L).apply()
        }

    var userHeight: Float?
        get() {
            return preferences.getFloat(PREF_USER_HEIGHT, 0f)
        }
        set(value) {
            preferencesEdit.putFloat(PREF_USER_HEIGHT, value ?: 0f).apply()
        }

    var userWeight: Float?
        get() {
            return preferences.getFloat(PREF_USER_WEIGHT, 0f)
        }
        set(value) {
            preferencesEdit.putFloat(PREF_USER_WEIGHT, value ?: 0f).apply()
        }

    var userGender: Boolean
    get() {
        return preferences.getBoolean(PREF_USER_GENDER, false)
    }
    set(value) {
        preferencesEdit.putBoolean(PREF_USER_GENDER, value).apply()
    }

    var userPhotoUrl: String?
        get() {
            return preferences.getString(PREF_USER_PHOTO, "")
        }
        set(value) {
            preferencesEdit.putString(PREF_USER_PHOTO, value).apply()
        }
}