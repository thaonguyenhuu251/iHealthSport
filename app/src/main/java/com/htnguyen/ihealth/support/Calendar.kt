package com.htnguyen.ihealth.support

import java.util.*

fun Calendar(timezone: TimeZone = TimeZone(), locale: Locale = Locale.getDefault()): Calendar =
    Calendar.getInstance(timezone, locale)

fun Calendar(
    year: Int,
    month: Int,
    day: Int,
    timezone: TimeZone = TimeZone(),
    locale: Locale = Locale.getDefault()
): Calendar {
    val calendar = Calendar.getInstance(timezone, locale)
    calendar.year = year
    calendar.month = month
    calendar.day = day
    return calendar
}

fun Calendar(
    time: Long,
    timezone: TimeZone = TimeZone(),
    locale: Locale = Locale.getDefault()
): Calendar {
    val calendar = Calendar.getInstance(timezone, locale)
    calendar.timeInMillis = time
    return calendar
}


var Calendar.year
    get() = get(Calendar.YEAR)
    set(value) = set(Calendar.YEAR, value)

var Calendar.month
    get() = get(Calendar.MONTH) + 1
    set(value) = set(Calendar.MONTH, value - 1)

var Calendar.day
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) = set(Calendar.DAY_OF_MONTH, value)

var Calendar.hour
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) = set(Calendar.HOUR_OF_DAY, value)

var Calendar.minute
    get() = get(Calendar.MINUTE)
    set(value) = set(Calendar.MINUTE, value)

var Calendar.second
    get() = get(Calendar.SECOND)
    set(value) = set(Calendar.SECOND, value)

val Calendar.dateInMillis: Long
    get() {
        val calendar = Calendar(timeInMillis)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

val Calendar.firstYear: Long
    get() {
        val calendar = Calendar(timeInMillis)
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

val Calendar.endYear: Long
    get() {
        val calendar = Calendar(timeInMillis)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.MONTH, 12)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

fun Calendar.endOfMonth(): Long {
    val calendar = Calendar(timeInMillis)
    calendar.add(Calendar.MONTH, 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.add(Calendar.DATE, -1)
    return calendar.timeInMillis
}

fun Calendar.firstOfMonth(): Calendar {
    set(Calendar.DAY_OF_MONTH, 1)
    return this
}

fun Calendar.lastOfMonth(): Calendar {
    add(Calendar.MONTH, 1)
    set(Calendar.DAY_OF_MONTH, 1)
    add(Calendar.DATE, -1)
    return this
}