package com.htnguyen.ihealth.model

data class Routes(
    val code: String,
    val routes: List<Route>,
    val uuid: String,
    val waypoints: List<Waypoint>
)