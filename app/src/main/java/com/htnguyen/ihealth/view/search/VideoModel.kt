package com.htnguyen.ihealth.view.search

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class VideoModel(
    var name: String? = null,
    var search: String? = null,
    var videourl: String? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "search" to search,
            "videourl" to videourl
        )
    }

}