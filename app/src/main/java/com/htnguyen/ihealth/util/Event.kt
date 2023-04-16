package com.htnguyen.ihealth.util

import com.htnguyen.ihealth.view.IHealthApplication

object Event {
    const val EVENT_OPEN_NOTIFICATION= "EVENT_OPEN_NOTIFICATION"

    const val EVENT_OPEN_SETTING= "EVENT_OPEN_SETTING"

    fun eventOpenNotification() {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_OPEN_NOTIFICATION to "open"))
    }

    fun eventOpenSetting() {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_OPEN_SETTING to "open"))
    }


}