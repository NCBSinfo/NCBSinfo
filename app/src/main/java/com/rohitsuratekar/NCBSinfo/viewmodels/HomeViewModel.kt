package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    val returnedRoute = MutableLiveData<Route>()
    val routeList = MutableLiveData<List<RouteData>>()
    val currentRoute = MutableLiveData<Route>()


    fun getRouteList(repository: Repository) {
        GetRoutes(repository, object : OnDataRetrieved {
            override fun routeReturned(route: Route) {
                Log.i(TAG, "Method Ignored")
            }

            override fun favoriteReturned(favRoute: Route) {
                Log.i(TAG, "Method Ignored")
            }

            override fun routeListReturned(returnedList: List<RouteData>) {
                routeList.postValue(returnedList)
            }

        }).execute()
    }

    fun getFavorite(repository: Repository) {
        GetFavorite(repository, object : OnDataRetrieved {
            override fun routeReturned(route: Route) {
                Log.i(TAG, "Method Ignored")
            }

            override fun routeListReturned(returnedList: List<RouteData>) {
                Log.i(TAG, "Method Ignored")
            }

            override fun favoriteReturned(favRoute: Route) {
                currentRoute.postValue(favRoute)
            }

        }).execute()
    }

    fun changeFavorite(routeData: RouteData, isFavorite: Boolean, repository: Repository) {
        ChangeFav(routeData, isFavorite, repository, object : OnDataRetrieved {
            override fun routeReturned(route: Route) {
                returnedRoute.postValue(route)
            }

            override fun routeListReturned(returnedList: List<RouteData>) {
                Log.i(TAG, "Method Ignored")
            }

            override fun favoriteReturned(favRoute: Route) {
                Log.i(TAG, "Method Ignored")
            }


        }).execute()
    }

    fun changeCurrentRoute(routeData: RouteData, repository: Repository) {
        ChangeRoute(routeData, repository, object : OnDataRetrieved {
            override fun routeListReturned(returnedList: List<RouteData>) {
                Log.i(TAG, "Method Ignored")
            }

            override fun favoriteReturned(favRoute: Route) {
                Log.i(TAG, "Method Ignored")
            }

            override fun routeReturned(route: Route) {
                returnedRoute.postValue(route)
            }

        }).execute()
    }

    class ChangeRoute(
        private val routeData: RouteData,
        private val repository: Repository,
        private val listener: OnDataRetrieved
    ) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val r = repository.data().getRouteByNumber(routeData.routeID)
            val t = repository.data().getTrips(r)
            listener.routeReturned(Route(r, t))
            return null
        }

    }

    class ChangeFav(
        private val routeData: RouteData,
        private val isFavorite: Boolean,
        private val repository: Repository,
        private val listener: OnDataRetrieved
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            if (isFavorite) {
                repository.data().changeFavoriteRoute(routeData)
            } else {
                repository.data().removeFavorite()
            }

            val r = repository.data().getRouteByNumber(routeData.routeID)
            val t = repository.data().getTrips(r)
            listener.routeReturned(Route(r, t))
            return null
        }

    }

    class GetRoutes(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            listener.routeListReturned(repository.data().getAllRoutes())
            return null
        }
    }

    class GetFavorite(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val routes = repository.data().getAllRoutes()
            var fav: RouteData? = null
            for (r in routes) {
                if (r.favorite == "yes") {
                    fav = r
                    break
                }
            }

            fav?.let {
                val trips = repository.data().getTrips(it)
                listener.favoriteReturned(Route(it, trips))
            } ?: kotlin.run {
                Log.i(TAG, "There was no favorite route. Making random route as favorite")
                repository.data().changeFavoriteRoute(routes.first())
                val trips = repository.data().getTrips(routes.first())
                routes.first().favorite = "yes"
                listener.favoriteReturned(Route(routes.first(), trips))
            }
            return null
        }

    }

    interface OnDataRetrieved {
        fun routeListReturned(returnedList: List<RouteData>)
        fun favoriteReturned(favRoute: Route)
        fun routeReturned(route: Route)
    }
}