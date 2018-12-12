package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository

class TransportRoutesViewModel : ViewModel() {

    val routeList = MutableLiveData<List<RouteData>>()
    val currentRoute = MutableLiveData<RouteData>()

    fun fetchRoutes(repository: Repository) {
        GetRoutes(repository, object : OnDataRetrieved {
            override fun routeListReturned(returnedList: List<RouteData>) {
                routeList.postValue(returnedList)
            }

        }).execute()
    }


    class GetRoutes(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            listener.routeListReturned(repository.data().getAllRoutes())
            return null
        }
    }

    fun changeCurrentRoute(routeData: RouteData) {
        currentRoute.postValue(routeData)
    }


    interface OnDataRetrieved {
        fun routeListReturned(returnedList: List<RouteData>)
    }

}