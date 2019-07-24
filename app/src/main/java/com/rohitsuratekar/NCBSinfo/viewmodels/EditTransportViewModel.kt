package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.serverTimestamp
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.EditTransportStep
import com.rohitsuratekar.NCBSinfo.models.Route
import java.util.*

class EditTransportViewModel : ViewModel() {

    val origin = MutableLiveData<String>()
    val destination = MutableLiveData<String>()
    val stepUpdate = MutableLiveData<MutableList<EditTransportStep>>()
    val tripList = MutableLiveData<MutableList<String>>()
    val tripSelected = MutableLiveData<Boolean>()
    val transportType = MutableLiveData<Int>()
    val transportFrequency = MutableLiveData<Int>()
    val frequencyDetails = MutableLiveData<MutableList<Int>>()
    val inputRouteID = MutableLiveData<Int>()
    val currentRoute = MutableLiveData<Route>()
    val routeDeleted = MutableLiveData<Boolean>()
    val selectedTrip = MutableLiveData<TripData>()


    fun setInputRouteID(routeID: Int) {
        inputRouteID.postValue(routeID)
    }

    fun setOrigin(string: String) {
        origin.postValue(string)
    }

    fun setDestination(string: String) {
        destination.postValue(string)
    }

    fun setRoute(route: Route) {
        currentRoute.postValue(route)
    }

    fun setSelectedTrip(tripData: TripData) {
        selectedTrip.postValue(tripData)
    }

    fun addSteps(list: List<EditTransportStep>) {
        val k = mutableListOf<EditTransportStep>()
        k.addAll(list)
        stepUpdate.postValue(k)
    }

    fun setType(type: Int) {
        transportType.postValue(type)
        updateConfirmState(Constants.EDIT_TYPE, true)
    }

    fun setFrequency(id: Int) {
        transportFrequency.postValue(id)
    }

    fun setFrequencyData(list: MutableList<Int>) {
        frequencyDetails.postValue(list)
        if (list.sum() > 0) {
            updateConfirmState(Constants.EDIT_FREQUENCY, true)
        } else {
            updateConfirmState(Constants.EDIT_FREQUENCY, false)
        }
    }

    fun updateReadState(step: Int) {
        val returnList = mutableListOf<EditTransportStep>()
        stepUpdate.value?.let {
            for (t in it) {
                if (t.number == step) {
                    t.isSeen = true
                }
                returnList.add(t)
            }
        }
        if (returnList.isNotEmpty()) {
            stepUpdate.postValue(returnList)
        }
    }

    fun updateConfirmState(step: Int, isConfirmed: Boolean) {
        val returnList = mutableListOf<EditTransportStep>()
        stepUpdate.value?.let {
            for (t in it) {
                if (t.number == step) {
                    t.isComplete = isConfirmed
                }
                returnList.add(t)
            }
        }
        if (returnList.isNotEmpty()) {
            stepUpdate.postValue(returnList)
        }
    }

    fun updateTrips(list: List<String>) {
        val k = mutableListOf<String>()
        k.addAll(list)
        tripList.postValue(k)
        updateConfirmState(Constants.EDIT_TRIPS, k.isNotEmpty())
    }

    fun updateTripSelection(value: Boolean) {
        tripSelected.postValue(value)
        updateConfirmState(Constants.EDIT_START_TRIP, value)
    }

    fun clearAllAttributes() {
        origin.postValue(null)
        destination.postValue(null)
        transportType.postValue(null)
        transportFrequency.postValue(null)
        tripList.postValue(null)
        frequencyDetails.postValue(null)
        tripSelected.postValue(null)

        val returnList = mutableListOf<EditTransportStep>()
        stepUpdate.value?.let {
            for (t in it) {
                t.isSeen = false
                t.isComplete = false
                returnList.add(t)
            }
        }
        stepUpdate.postValue(returnList)
    }

    fun deleteRoute(repository: Repository, route: Route, selectedTrip: TripData?) {
        DeleteRoute(repository, route, selectedTrip, object : OnDeleteRoute {
            override fun deleted() {
                routeDeleted.postValue(true)
            }

        }).execute()
    }

    class DeleteRoute(
        private val repository: Repository,
        private val route: Route,
        private val selectedTrip: TripData?,
        private val listener: OnDeleteRoute
    ) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            selectedTrip?.let {
                repository.data().updateModifiedDate(route.routeData, Calendar.getInstance().serverTimestamp())
                repository.data().deleteTrip(it)
            } ?: kotlin.run {
                repository.data().deleteRoute(route.routeData)
            }
            listener.deleted()
            return null
        }
    }

    interface OnDeleteRoute {
        fun deleted()
    }
}