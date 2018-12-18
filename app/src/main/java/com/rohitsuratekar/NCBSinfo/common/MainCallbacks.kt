package com.rohitsuratekar.NCBSinfo.common

import com.rohitsuratekar.NCBSinfo.models.Contact

interface MainCallbacks {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun navigate(option: Int)
    fun showRouteList(currentRoute: Int)
    fun showContact(contact: Contact)
}