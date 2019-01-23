package com.rohitsuratekar.NCBSinfo.fragments


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportTripAdapter
import com.rohitsuratekar.NCBSinfo.common.*
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import kotlinx.android.synthetic.main.fragment_add_trips.*
import java.util.*

class AddTripsFragment : EditFragment() {

    private lateinit var adapter: EditTransportTripAdapter
    private var itemList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trips, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditTransportTripAdapter(itemList)
        adapter.setUndoOn(false)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                sharedModel.updateTrips(itemList)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                itemList.sort()
                adapter.notifyDataSetChanged()
                sharedModel.updateTrips(itemList)
                sharedModel.updateTripSelection(false)
                checkEmptyList()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_TRIPS)
        callback?.setFragmentTitle(R.string.et_add_trips)
        et_trip_add_fab.setOnClickListener { selectTime() }
        et_trip_next.setOnClickListener { callback?.navigate(Constants.EDIT_START_TRIP) }
        et_trip_previous.setOnClickListener { callback?.navigateWithPopback() }
        setUpRecycler()
    }

    private fun setUpRecycler() {
        checkOldData()
        et_trip_recycler.layoutManager = LinearLayoutManager(context)
        et_trip_recycler.adapter = adapter
        et_trip_recycler.setHasFixedSize(true)
        et_trip_recycler.addItemDecoration(SwipeItemDecorator())
        val mItemTouchHelper = ItemTouchHelper(TouchHelper.get(context!!, et_trip_recycler))
        mItemTouchHelper.attachToRecyclerView(et_trip_recycler)
        checkEmptyList()
    }

    private fun checkEmptyList() {
        if (itemList.isEmpty()) {
            et_trip_holder.hideMe()
        } else {
            et_trip_holder.showMe()
        }
    }

    private fun getFormattedDate(hour: Int, minute: Int): String {
        var returnString = hour.toString()
        if (returnString.length == 1) {
            returnString = "0$returnString"
        }
        returnString = if (minute.toString().length == 1) {
            "$returnString:0$minute"
        } else {
            "$returnString:$minute"
        }
        return returnString
    }

    private fun selectTime() {
        val cal = Calendar.getInstance()
        TimePickerDialog(context!!, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val t = getFormattedDate(hourOfDay, minute)
            if (t in itemList) {
                context?.toast(getString(R.string.et_same_trip_warning, convertTimeFormat(t)))
            } else {
                itemList.add(t)
                itemList.sort()
                adapter.notifyDataSetChanged()
                checkEmptyList()
                sharedModel.updateTripSelection(false)
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    private fun checkOldData() {

        sharedModel.tripList.value?.let {
            itemList.clear()
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
            checkEmptyList()
        }
    }

}
