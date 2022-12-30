package com.htnguyen.ihealth.support

import java.text.SimpleDateFormat
import java.util.*

fun SimpleDateFormat(
    pattern: String,
    locale: Locale = Locale.JAPAN,
    timezone: TimeZone = TimeZone()
) = SimpleDateFormat(pattern, locale).apply { timeZone = timezone }