package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportAdjustAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import kotlinx.android.synthetic.main.fragment_adjust_trip.*

class AdjustTripFragment : EditFragment() {

    private var tripList = mutableListOf<String>()
    private lateinit var adapter: EditTransportAdjustAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adjust_trip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_START_TRIP)
        subscribe()
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = EditTransportAdjustAdapter(tripList)
        adjust_recycler.adapter = adapter
        adjust_recycler.layoutManager = LinearLayoutManager(context)

        if (tripList.isEmpty()) {
            adjust_holder.hideMe()
            adjust_note.text = getString(R.string.et_adjust_warning)
        } else {
            adjust_holder.showMe()
            adjust_note.text = getString(R.string.et_adjust_note)
        }
    }

    private fun subscribe() {

    }

}
