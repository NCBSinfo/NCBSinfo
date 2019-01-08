package com.rohitsuratekar.NCBSinfo.common

interface EditCallbacks {

    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun navigate(option: Int)
}