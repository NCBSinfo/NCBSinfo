package com.rohitsuratekar.NCBSinfo.models

data class Location(
    var name: String,
    var oldName: String,
    var details: String,
    var building: String,
    var floor: Int,
    var type: String
)