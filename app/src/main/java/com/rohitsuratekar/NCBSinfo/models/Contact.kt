package com.rohitsuratekar.NCBSinfo.models

class Contact {

    enum class AREA {
        NAME, EXTENSION, EXTRA_NUMBERS, DETAILS
    }

    var type: String? = null
    var name: String? = null
    var primaryExtension: String? = null
    var otherExtensions: String? = null
    var details: String? = null
    var searchString = ""
    val searchArea = mutableListOf<AREA>()

    fun extraExtensions(): List<String> {
        val tempList = mutableListOf<String>()
        otherExtensions?.let {
            for (c in it.split(",")) {
                tempList.add(c)
            }
        }
        return tempList
    }

}