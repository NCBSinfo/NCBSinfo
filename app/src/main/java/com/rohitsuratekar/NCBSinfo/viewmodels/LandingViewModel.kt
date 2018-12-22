package com.rohitsuratekar.NCBSinfo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.CheckRoutes
import com.rohitsuratekar.NCBSinfo.common.OnFinishRetrieving
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

class LandingViewModel : ViewModel() {

    val status = MutableLiveData<String>()
    val dataLoaded = MutableLiveData<Boolean>()

    fun checkAvailableRoutes(repository: Repository) {

        CheckRoutes(repository, object : OnFinishRetrieving {
            override fun returnRoutes(routeList: List<Route>) {}

            override fun dataLoadFinished() {
                dataLoaded.postValue(true)
            }

            override fun changeStatus(statusNote: String) {
                status.postValue(statusNote)
            }

        }).execute()
    }


}