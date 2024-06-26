package com.htnguyen.ihealth.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SocialHealth(
    var idSocial: Long? = null,
    var typeSocial: Int? = null,
    var createBy: String? = null,
    var listMember: String? = "6000",
    var nameSocial: String? = null,
    var firstDate: Long? = null,
    var endDate: Long? = null,
    var followStep: Int? = null,
    var imageUrl: String? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idSocial" to idSocial,
            "createBy" to createBy,
            "typeSocial" to typeSocial,
            "listMember" to listMember,
            "nameSocial" to nameSocial,
            "firstDate" to firstDate,
            "endDate" to endDate,
            "followStep" to followStep,
            "imageUrl" to imageUrl,
        )
    }
}
