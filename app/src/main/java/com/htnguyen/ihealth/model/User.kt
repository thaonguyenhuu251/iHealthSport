package com.htnguyen.ihealth.model

import java.io.Serializable

data class User(
    var idUser: String? = null,
    var idAccount: String? = "",
    var name: String? = "",
    var birthDay: Long? = 0L,
    var passWord: String? = "",
    var photoUrl: String? = "",
    var gender: Boolean? = false,
    var height: Float? = 0f,
    var weight: Float? = 0f
) :  Serializable {

}