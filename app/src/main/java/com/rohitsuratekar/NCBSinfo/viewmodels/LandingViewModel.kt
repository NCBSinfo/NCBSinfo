package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.*
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.DataUpdateModel
import com.rohitsuratekar.NCBSinfo.models.Route
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "LandingViewModel"

class LandingViewModel : ViewModel() {

    val status = MutableLiveData<String>()
    val dataLoaded = MutableLiveData<Boolean>()
    val dataUpdated = MutableLiveData<Boolean>()

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


    fun checkDataUpdate(repository: Repository) {

        if (repository.prefs().updateVersion() < Constants.UPDATE_VERSION) {

            //TODO: Need proper method to update sequentially
            val updateList = mutableListOf<DataUpdateModel>()
            updateList.addAll(updateMay19(repository.app().baseContext)) // For 3 May 2019 Update
            updateList.addAll(updateJune19(repository.app().baseContext)) // For 4 June 2019 Update

            CheckUpdates(repository, updateList, object : OnUpdate {
                override fun updateCompleted() {
                    dataUpdated.postValue(true)
                }
            }).execute()
        } else {
            Log.i(TAG, "Database is up-to date")
            dataUpdated.postValue(true)
        }

    }

    class CheckUpdates(
        private val repository: Repository,
        private val dataList: List<DataUpdateModel>,
        private val listener: OnUpdate
    ) :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            for (data in dataList) {
                Log.i(TAG, "Started Update ${data.origin} - ${data.destination} ${data.type}")
                var routeNo = repository.data().isRouteThere(data.origin, data.destination, data.type)
                if ((routeNo == 0) and data.createNew) {
                    routeNo = repository.data().addRoute(RouteData().apply {
                        origin = data.origin.trim().toLowerCase(Locale.getDefault())
                        destination = data.destination.trim().toLowerCase(Locale.getDefault())
                        type = data.type.trim().toLowerCase(Locale.getDefault())
                        author = Constants.DEFAULT_AUTHOR
                        modifiedOn = data.updateDate
                        createdOn = data.updateDate
                        syncedOn = data.updateDate
                        favorite = "no"
                    }).toInt()
                    Log.i(TAG, "New Route Created")
                }
                if (routeNo != 0) {
                    val route = repository.data().getRouteByNumber(routeNo)
                    if (data.deleteRoute) {
                        repository.data().deleteRoute(route)
                        Log.i(TAG, "Route Deleted")
                    } else {
                        if ((data.replaceAll) and data.tripList.isNotEmpty()) {
                            val tripList = repository.data().getTrips(route)
                            for (t in tripList) {
                                repository.data().deleteTrip(t)
                            }
                            Log.i(TAG, "Old Trips Deleted")
                        }

                        val tripMap = HashMap<Int, TripData>()
                        for (t in repository.data().getTrips(route)) {
                            tripMap[t.day] = t
                        }
                        for (t in data.tripList) {
                            if (tripMap.containsKey(t.day)) {
                                repository.data().deleteTrip(tripMap[t.day]!!)
                                Log.i(TAG, "Old Trips Deleted")
                            }
                            t.routeID = route.routeID
                            repository.data().addTrips(t)
                            repository.data().updateModifiedDate(route, data.updateDate)
                            Log.i(TAG, "Added New Trip")
                        }
                    }

                }
            }
            Log.i(TAG, "Database Updated")
            repository.prefs().updateVersion(Constants.UPDATE_VERSION)
            listener.updateCompleted()
            return null
        }

    }

    interface OnUpdate {
        fun updateCompleted()
    }


}