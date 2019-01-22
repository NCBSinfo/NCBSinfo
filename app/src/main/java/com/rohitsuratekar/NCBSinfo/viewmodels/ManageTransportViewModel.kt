package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.CheckRoutes
import com.rohitsuratekar.NCBSinfo.common.OnFinishRetrieving
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route

class ManageTransportViewModel : ViewModel() {
    val routeList = MutableLiveData<List<Route>>()
    val routeDeleted = MutableLiveData<List<Route>>()
    val lastModified = MutableLiveData<String>()
    val favoriteChanged = MutableLiveData<Boolean>()

    fun getRouteList(repository: Repository) {
        GetData(repository, object : OnDataRetrieved {
            override fun lastModified(date: String) {}

            override fun resetRoutes() {}

            override fun routeDeleted(data: List<Route>) {}

            override fun updateRoutes(data: List<Route>) {
                routeList.postValue(data)
            }
        }).execute()
    }

    fun resetRoutes(repository: Repository) {
        ResetRoutes(repository, object : OnDataRetrieved {
            override fun lastModified(date: String) {}
            override fun updateRoutes(data: List<Route>) {}

            override fun routeDeleted(data: List<Route>) {}

            override fun resetRoutes() {
                remakeRoutes(repository)
            }

        }).execute()
    }

    fun getLastUpdate(repository: Repository) {

        LastUpdateFromData(repository, object : OnDataRetrieved {
            override fun updateRoutes(data: List<Route>) {

            }

            override fun routeDeleted(data: List<Route>) {
            }

            override fun lastModified(date: String) {
                lastModified.postValue(date)
            }

            override fun resetRoutes() {
            }
        }).execute()

    }

    private fun remakeRoutes(repository: Repository) {
        CheckRoutes(repository, object : OnFinishRetrieving {
            override fun returnRoutes(routeList: List<Route>) {
                routeDeleted.postValue(routeList)
            }

            override fun dataLoadFinished() {}

            override fun changeStatus(statusNote: String) {}

        }).execute()
    }

    fun deleteRoute(repository: Repository, route: Route) {

        DeleteRoute(repository, route, object : OnDataRetrieved {
            override fun resetRoutes() {
                remakeRoutes(repository)
            }

            override fun lastModified(date: String) {}
            override fun updateRoutes(data: List<Route>) {}

            override fun routeDeleted(data: List<Route>) {
                routeDeleted.postValue(data)
            }

        }).execute()

    }

    fun updateFavorite(repository: Repository, route: Route) {

        UpdateFavorite(repository, route, object : OnDataRetrieved {
            override fun updateRoutes(data: List<Route>) {
            }

            override fun routeDeleted(data: List<Route>) {
            }

            override fun lastModified(date: String) {
                favoriteChanged.postValue(true)
            }

            override fun resetRoutes() {
            }

        }).execute()

    }


    class UpdateFavorite(
        private val repository: Repository,
        private val route: Route,
        private val listener: OnDataRetrieved
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            repository.data().changeFavoriteRoute(route.routeData)
            listener.lastModified("--:--")
            return null
        }

    }


    class LastUpdateFromData(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            listener.lastModified(repository.data().lastModifiedOn())
            return null
        }

    }


    class GetData(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val routeList = repository.data().getAllRoutes()
            val returnList = mutableListOf<Route>()
            for (r in routeList) {
                returnList.add(Route(r, repository.data().getTrips(r)).apply {
                    isFavorite = r.favorite == "yes"
                })
            }
            listener.updateRoutes(returnList)
            return null
        }
    }

    class ResetRoutes(private val repository: Repository, private val listener: OnDataRetrieved) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            repository.data().deleteAll()
            listener.resetRoutes()
            return null
        }

    }

    class DeleteRoute(
        private val repository: Repository,
        private val route: Route,
        private val listener: OnDataRetrieved
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            repository.data().deleteRoute(route.routeData)
            val routeList = repository.data().getAllRoutes()
            if (routeList.isEmpty()) {
                listener.resetRoutes()
            } else {
                val returnList = mutableListOf<Route>()
                for (r in routeList) {
                    returnList.add(Route(r, repository.data().getTrips(r)))
                }
                listener.routeDeleted(returnList)
            }
            return null
        }

    }

    interface OnDataRetrieved {
        fun updateRoutes(data: List<Route>)
        fun routeDeleted(data: List<Route>)
        fun lastModified(date: String)
        fun resetRoutes()
    }
}