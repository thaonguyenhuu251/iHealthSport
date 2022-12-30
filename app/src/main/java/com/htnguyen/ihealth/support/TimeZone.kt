package com.htnguyen.ihealth.support

import java.util.*

fun TimeZone(id: String = "GMT+07:00"): TimeZone =
    TimeZone.getTimeZone(id)