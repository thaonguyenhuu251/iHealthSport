package com.htnguyen.ihealth.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object Constant {
    const val IMAGE_IDENTIFY_PATH = "poketo_image"

    const val ARG_TASK_ID = "arg_task_id"
    const val ARG_TASK_TYPE = "arg_task_type"
    const val ARG_TASK_FROM_PUSH = "arg_task_from_push"
    const val ARG_TASK_EDIT_TYPE = "arg_task_edit_type"
    const val ARG_TASK = "arg_task"
    const val ARG_USER = "arg_user"

    const val USER_GROUP_NAME = "regular_user"

    const val FILTER_BY_CREATOR = "creator"
    const val FILTER_BY_ASSIGNEE = "assignee"
    const val STATUS_INCOMPLETE = "incomplete"
    const val PRIORITY_HIGH = "high"

    const val MAX_SIZE_AVATAR = 1000 * 1000 * 5
    const val MAX_SIZE_DOCUMENT = 1000 * 1000 * 100
    val VALID_DOCUMENT_EXTENSION =
        arrayOf("txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "png", "mp3",
            "mov", "mp4", "jpeg", "jpg", "gif", "zip")

    const val ERROR_CODE_DELETE_ITEM = 404
    const val ERROR_CODE_MAINTENANCE = 503

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