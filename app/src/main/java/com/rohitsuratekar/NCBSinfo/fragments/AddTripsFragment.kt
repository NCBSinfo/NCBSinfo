package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditFragment

class AddTripsFragment : EditFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_TRIPS)
        subscribe()
    }

    private fun subscribe() {

    }

}
