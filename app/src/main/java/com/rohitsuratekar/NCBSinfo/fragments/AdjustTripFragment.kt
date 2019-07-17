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

class AdjustTripFragment : EditFragment(), EditTransportAdjustAdapter.OnTripAdjust {


    private var tripList = mutableListOf<String>()
    private lateinit var adapter: EditTransportAdjustAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adjust_trip, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditTransportAdjustAdapter(tripList, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_START_TRIP)
        callback?.setFragmentTitle(R.string.et_adjust_trips)
        et_adjust_next.setOnClickListener { callback?.navigate(Constants.EDIT_CONFIRM) }
        et_adjust_previous.setOnClickListener { callback?.navigateWithPopback() }
        checkOldData()
        setupRecycler()
    }

    private fun setupRecycler() {
        adjust_recycler.adapter = adapter
        adjust_recycler.layoutManager = LinearLayoutManager(context)
        checkEmptyList()
    }

    private fun checkEmptyList() {
        if (tripList.isEmpty()) {
            adjust_holder.hideMe()
            adjust_note.text = getString(R.string.et_adjust_warning)
        } else {
            adjust_holder.showMe()
            adjust_note.text = getString(R.string.et_adjust_note)
        }
    }

    private fun checkOldData() {
        sharedModel.tripList.value?.let {
            tripList.clear()
            tripList.addAll(it)
            adapter.notifyDataSetChanged()
            checkEmptyList()
        }
        sharedModel.tripSelected.value?.let {
            adapter.tripSelected(it)
        }
    }

    override fun firstTripSelected(position: Int) {
        val tempList = mutableListOf<String>()
        val newList = mutableListOf<String>()
        for (i in 0 until tripList.size) {
            if (i < position) {
                tempList.add(tripList[i])
            } else {
                newList.add(tripList[i])
            }
        }
        for (i in tempList) {
            newList.add(i)
        }
        tripList.clear()
        tripList.addAll(newList)
        sharedModel.updateTrips(newList)
        adapter.tripSelected(true)
        adapter.notifyDataSetChanged()
        sharedModel.updateTripSelection(true)
    }

}
