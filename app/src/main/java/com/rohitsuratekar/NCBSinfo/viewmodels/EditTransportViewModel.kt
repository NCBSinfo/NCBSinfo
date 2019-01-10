package com.rohitsuratekar.NCBSinfo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditTransportStep

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


    fun setInputRouteID(routeID: Int) {
        inputRouteID.postValue(routeID)
    }

    fun setOrigin(string: String) {
        origin.postValue(string)
    }

    fun setDestination(string: String) {
        destination.postValue(string)
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
        updateConfirmState(Constants.EDIT_TRIPS, !k.isEmpty())
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
}