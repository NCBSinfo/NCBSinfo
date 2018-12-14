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


    fun fetchRoute(repository: Repository, routeID: Int) {
        GetRouteDetails(repository, routeID, object : OnDataRetrieved {
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
                listener.fetchedRoute(Route(it, trips))
            } ?: kotlin.run {
                Log.i(TAG, "There was no such route found. Selecting first Route from the database")
                val trips = repository.data().getTrips(routes.first())
                listener.fetchedRoute(Route(routes.first(), trips))
            }
            return null
        }

    }

    interface OnDataRetrieved {
        fun fetchedRoute(route: Route)
    }

}