package com.htnguyen.ihealth.util

import java.util.*

internal object Util {

    /**
     * Get calendar with zeroed time (hours, minutes, seconds)
     *
     * @return Calendar with start of the day
     */
    internal val calendar: Calendar
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar
        }


    /**
     * Formula to convert steps to kilometers
     *
     * @param steps steps
     * @return kilometers
     */
    internal fun stepsToMeters(steps: Number): Double {
        return steps.toInt() * 0.762 / 1000
    }

}
