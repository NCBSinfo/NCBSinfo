package com.rohitsuratekar.NCBSinfo.common

interface MainCallbacks {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun navigate(option: Int)
    fun showRouteList(currentRoute: Int)
}