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
}