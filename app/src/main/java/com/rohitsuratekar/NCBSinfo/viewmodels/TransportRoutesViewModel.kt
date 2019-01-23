package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

class TransportRoutesViewModel : ViewModel() {

    val routeList = MutableLiveData<List<RouteData>>()
    val currentRoute = MutableLiveData<Route>()

    fun fetchRoutes(repository: Repository) {
        GetRoutes(repository, object : OnDataRetrieved {
            override fun currentRoute(route: Route) {

            }

            override fun routeListReturned(returnedList: List<RouteData>) {
                routeList.postValue(returnedList)
            }

        }).execute()
    }

    fun changeCurrentRoute(repository: Repository, routeData: RouteData) {
        GetRouteData(repository, routeData, object : OnDataRetrieved {
            override fun routeListReturned(returnedList: List<RouteData>) {
            }

            override fun currentRoute(route: Route) {
                currentRoute.postValue(route)
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

    class GetRouteData(
        private val repository: Repository,
        private val routeData: RouteData,
        private val listener: OnDataRetrieved
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            for (r in repository.data().getAllRoutes()) {
                if (routeData.routeID == r.routeID) {
                    listener.currentRoute(Route(r, repository.data().getTrips(r)))
                }
            }
            return null
        }

    }


    interface OnDataRetrieved {
        fun routeListReturned(returnedList: List<RouteData>)
        fun currentRoute(route: Route)
    }

}