package com.htnguyen.ihealth.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ActivityDaily(
    var step: Int? = null,
    var followStep: Int? = 6000,
    var timeActive: Float? = null,
    var calo: Float? = null,
)  {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "step" to step,
            "followStep" to followStep,
            "timeActive" to timeActive,
            "calo" to calo,
        )
    }
}