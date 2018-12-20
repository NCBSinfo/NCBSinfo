package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

class ManageTransportViewModel : ViewModel() {
    val routeList = MutableLiveData<List<Route>>()

    fun getRouteList(repository: Repository) {
        GetData(repository, object : OnDataRetrieved {
            override fun updateRoutes(data: List<Route>) {
                routeList.postValue(data)
            }
        }).execute()
    }


    class GetData(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val routeList = repository.data().getAllRoutes()
            val returnList = mutableListOf<Route>()
            for (r in routeList) {
                returnList.add(Route(r, repository.data().getTrips(r)))
            }
            listener.updateRoutes(returnList)
            return null
        }


    }

    interface OnDataRetrieved {
        fun updateRoutes(data: List<Route>)
    }
}