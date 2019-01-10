package com.rohitsuratekar.NCBSinfo.common

import androidx.navigation.NavDirections
import com.rohitsuratekar.NCBSinfo.models.Contact

interface MainCallbacks {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun navigate(option: Int)
    fun showRouteList(currentRoute: Int)
    fun showContact(contact: Contact)
    fun editRoute(navDirections: NavDirections)
}