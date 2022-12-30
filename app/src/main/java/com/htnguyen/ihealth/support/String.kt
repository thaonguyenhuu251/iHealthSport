package com.htnguyen.ihealth.support

import kotlin.text.toInt
import kotlin.text.toFloat


fun String.toInt(): Int? {
    if (!Regex("^[0-9]+$").matches(this)) return null
    return toInt()
}

fun String.toFloat(): Float? {
    if (!Regex("^(?=.*?[0-9])[0-9]*[.]?[0-9]*$").matches(this)) return null
    return toFloat()
}