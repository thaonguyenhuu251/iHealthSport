package com.htnguyen.ihealth.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object Constant {

    const val USER_ID = "user_id"
    const val USER_PHONE = "user_phone"
    const val USER_OTP = "user_otp"
    const val USER_PASSWORD = "user_password"

    const val USER_BIRTHDAY = "user_birthday"

    const val CHALLENGE_POST = "challenge_post"
    const val CHALLENGE_FIRST_PERSON = "challenge_first_person"
    const val CHALLENGE_FIRST_TEAM = "challenge_first_team"
    const val CHALLENGE_FARTHEST_PERSON = "challenge_farthest_person"
    const val CHALLENGE_FARTHEST_TEAM = "challenge_farthest_team"

    const val TYPE_PROFILE = "type_profile"
    const val TYPE_CALENDAR = "type_calendar"
    const val KEY_PATH_IMAGE = "key_path_image"

    const val DATE_FORMAT = "yyyy_MM_dd"

    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }

}