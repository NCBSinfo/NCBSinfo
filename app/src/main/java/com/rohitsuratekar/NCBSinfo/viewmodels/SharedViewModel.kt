package com.rohitsuratekar.NCBSinfo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.models.Route

class SharedViewModel : ViewModel() {

    val currentRoute = MutableLiveData<Route>()

    fun changeCurrentRoute(route: Route) {
        currentRoute.postValue(route)
    }
}