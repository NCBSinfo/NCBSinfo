package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

private const val TAG = "TimetableViewModel"

class TimetableViewModel : ViewModel() {

    val returnedRoute = MutableLiveData<Route>()
    val reverseRoute = MutableLiveData<Route?>()


    fun fetchRoute(repository: Repository, routeID: Int) {
        GetRouteDetails(repository, routeID, object : OnDataRetrieved {
            override fun hasReverse(route: Route?) {
                reverseRoute.postValue(route)
            }

            override fun fetchedRoute(route: Route) {
                returnedRoute.postValue(route)
            }
        }).execute()
    }

    class GetRouteDetails(
        private val repository: Repository,
        private val routeID: Int,
        private val listener: OnDataRetrieved
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val routes = repository.data().getAllRoutes()
            var returnedRoute: RouteData? = null
            for (r in routes) {
                if (r.routeID == routeID) {
                    returnedRoute = r
                    break
                }
            }

            returnedRoute?.let {
                val trips = repository.data().getTrips(it)
                checkReverse(it)
                listener.fetchedRoute(Route(it, trips))
            } ?: kotlin.run {
                Log.i(TAG, "There was no such route found. Selecting first Route from the database")
                val trips = repository.data().getTrips(routes.first())
                checkReverse(routes.first())
                listener.fetchedRoute(Route(routes.first(), trips))
            }
            return null
        }

        private fun checkReverse(route: RouteData) {
            val id = repository.data().isRouteThere(route.destination!!, route.origin!!, route.type!!)
            if (id > 0) {
                val reverseRoute = repository.data().getRouteByNumber(id)
                val reverseTrips = repository.data().getTrips(reverseRoute)
                listener.hasReverse(Route(reverseRoute, reverseTrips))
            } else {
                listener.hasReverse(null)
            }
        }


    }

    interface OnDataRetrieved {
        fun fetchedRoute(route: Route)
        fun hasReverse(route: Route?)
    }

}