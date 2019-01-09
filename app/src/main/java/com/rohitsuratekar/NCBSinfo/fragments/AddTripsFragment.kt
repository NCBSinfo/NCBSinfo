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
                sharedModel.updateTrips(itemList)
                checkEmptyList()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_TRIPS)
        et_trip_add_fab.setOnClickListener { selectTime() }
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

    private fun selectTime() {
        val cal = Calendar.getInstance()
        TimePickerDialog(context!!, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            itemList.add("$hourOfDay : $minute")
            adapter.notifyDataSetChanged()
            checkEmptyList()
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
