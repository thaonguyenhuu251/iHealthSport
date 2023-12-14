package com.htnguyen.ihealth.util

import com.htnguyen.ihealth.view.IHealthApplication

object Event {
    const val EVENT_OPEN_NOTIFICATION = "EVENT_OPEN_NOTIFICATION"

    const val EVENT_OPEN_SETTING = "EVENT_OPEN_SETTING"
    const val EVENT_CHANGE_FOLLOW_WATER = "EVENT_CHANGE_FOLLOW_WATER"
    const val EVENT_CHANGE_FOLLOW_STEP = "EVENT_CHANGE_FOLLOW_STEP"
    const val EVENT_CHANGE_LANGUAGE = "EVENT_CHANGE_LANGUAGE"

    fun eventOpenNotification() {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_OPEN_NOTIFICATION to "open"))
    }

    fun eventOpenSetting() {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_OPEN_SETTING to "open"))
    }

    fun eventChangeFollowWater(followWater: Int) {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_CHANGE_FOLLOW_WATER to followWater))
    }

    fun eventChangeFollowStep(followStep: Int) {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_CHANGE_FOLLOW_STEP to followStep))
    }

    fun eventChangeLanguage() {
        IHealthApplication.eventBus.onNext(hashMapOf(EVENT_CHANGE_LANGUAGE to "LANGUAGE"))
    }


}