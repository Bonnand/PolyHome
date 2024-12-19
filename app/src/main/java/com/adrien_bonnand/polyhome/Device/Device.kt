package com.adrien_bonnand.polyhome.Device

data class Device(
    val id : String,
    val availableCommands : List<String>,
    val opening: Int,
    val openingMode: Int,
    val power: Int,
    val type: String
)