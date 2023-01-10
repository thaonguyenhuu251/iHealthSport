package com.htnguyen.ihealth.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class HealthDaily(
    var heartBeat: Float? = null,
    var oxyInBlood: Float? = null,
    var fellingToday: Int? = null,
    var stress: Int? = null,

)  {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "heartBeat" to heartBeat,
            "oxyInBlood" to oxyInBlood,
            "fellingToday" to fellingToday,
            "stress" to stress
        )
    }
}