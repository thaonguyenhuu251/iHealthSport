package com.htnguyen.ihealth.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object Constant {

    const val USER_ID = "user_id"
    const val USER_PHONE = "user_phone"
    const val USER_ACCOUNT = "user_account"
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

    const val LG_VIETNAMESE = "vietnamese"
    const val LG_ENGLISH = "english"
    const val LG_RUSSIAN = "russia"
    const val LG_LAOS = "lao"
    const val LG_THAI = "thai"
    const val LG_MYANMAR = "myanmar"
    const val LG_KOREAN = "korean"
    const val LG_CHINESE = "chinese"
    const val LG_JAPANESE = "japanese"
    const val LG_FILIPINO = "filipino"
    const val LG_INDONESIAN = "indonesian"
    const val LG_SPANISH = "spanish"
    const val LG_FRENCH = "french"
    const val LG_INDIAN = "indian"
    const val LG_GERMAN = "german"
    const val LG_ITALIAN = "italian"

    const val COLOR_BLUE = "color_blue"
    const val COLOR_RED = "color_red"
    const val COLOR_PINK = "color_pink"
    const val COLOR_DARKPINK = "color_darkpink"
    const val COLOR_VIOLET = "color_violet"
    const val COLOR_SKYBLUE = "color_skyblue"
    const val COLOR_GREEN = "color_green"
    const val COLOR_GREY = "color_grey"
    const val COLOR_BROWN = "color_brown"

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