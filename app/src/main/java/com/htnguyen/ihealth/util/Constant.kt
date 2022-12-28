package com.htnguyen.ihealth.util

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

}