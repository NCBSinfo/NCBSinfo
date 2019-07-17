package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

class SelectTripDayViewModel : ViewModel() {

    val currentRoute = MutableLiveData<Route>()

    fun extractRoute(repository: Repository, routeNo: Int) {
        GetRouteInfo(repository, routeNo, object : OnRouteDataExtracted {
            override fun route(route: Route) {
                currentRoute.postValue(route)
            }

        }).execute()
    }

    class GetRouteInfo(
        private val repository: Repository,
        private val routeNo: Int,
        private val listener: OnRouteDataExtracted
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val route = repository.data().getRouteByNumber(routeNo)
            val trips = repository.data().getTrips(route)
            listener.route(Route(route, trips))
            return null
        }

    }

    interface OnRouteDataExtracted {
        fun route(route: Route)
    }

}