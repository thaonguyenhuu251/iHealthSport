package com.htnguyen.ihealth.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EatAndDrink(
    var kcal: Float? = null,
    var followKcal: Float? = null,
    var water: Int? = null,
    var followWater: Int? = null,
    var waterMil: Int? = null
)  {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "kcal" to kcal,
            "followKcal" to followKcal,
            "water" to water,
            "followWater" to followWater,
            "waterMil" to waterMil
        )
    }
}