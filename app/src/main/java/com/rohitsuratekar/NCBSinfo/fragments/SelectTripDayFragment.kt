package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.SelectTripDayAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.viewmodels.SelectTripDayViewModel
import kotlinx.android.synthetic.main.fragment_select_trip_day.*
import java.util.*

class SelectTripDayFragment : EditFragment(), SelectTripDayAdapter.OnDaySelected {


    data class SelectTripDayModel(
        val title: Int,
        val subtitle: Int,
        val note: String,
        val id: Int,
        val freq: MutableList<Int>,
        val trips: List<String>
    )

    private lateinit var viewModel: SelectTripDayViewModel
    private lateinit var adapter: SelectTripDayAdapter
    private var itemList = mutableListOf<SelectTripDayModel>()
    private var currentRoute: Route? = null
    private var currentFrequency = mutableListOf<Int>()
    private val dayList = mutableListOf(
        Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
        Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
    )
    private val stringList = mutableListOf(
        R.string.sunday,
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
        R.string.saturday
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_trip_day, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SelectTripDayViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.setFragmentTitle(R.string.et_select_day)
        subscribe()
        adapter = SelectTripDayAdapter(itemList, this)
        et_sd_recycler.adapter = adapter
        et_sd_recycler.layoutManager = LinearLayoutManager(context)
        et_sd_new.setOnClickListener {
            sharedModel.clearAllAttributes()
            sharedModel.setInputRouteID(-1)
            callback?.navigate(Constants.EDIT_START_EDITING)
        }

    }


    private fun subscribe() {
        sharedModel.inputRouteID.observe(this, Observer {
            if (it == -1) {
                callback?.navigate(Constants.EDIT_START_EDITING)
            } else {
                viewModel.extractRoute(repository, it)
            }
        })

        viewModel.currentRoute.observe(this, Observer {
            currentRoute = it
            updateSharedModel()
        })
    }

    private fun getType(type: String): Int {
        return when (type) {
            "shuttle" -> R.id.et_type_option1
            "ttc" -> R.id.et_type_option2
            "buggy" -> R.id.et_type_option3
            else -> R.id.et_type_option4
        }
    }

    private fun updateSharedModel() {
        val frequencyDetails = mutableListOf(0, 0, 0, 0, 0, 0, 0)
        currentRoute?.let {
            sharedModel.setOrigin(it.routeData.origin!!.toUpperCase())
            sharedModel.setDestination(it.routeData.destination!!.toUpperCase())
            sharedModel.setType(getType(it.routeData.type!!))
            for (t in it.tripData) {
                frequencyDetails[dayList.indexOf(t.day)] = 1
            }
            sharedModel.setFrequencyData(frequencyDetails)
            currentFrequency.addAll(frequencyDetails)
            sharedModel.updateTripSelection(true)

            if (frequencyDetails.sum() == 1) {
                sharedModel.setFrequency(R.id.et_fq_all_days)
                sharedModel.updateTrips(it.tripData[0].trips!!)
                callback?.navigate(Constants.EDIT_START_EDITING)
            } else {
                updateUI()
            }
        }
    }

    private fun updateUI() {
        itemList.clear()
        currentRoute?.let {
            et_sd_route.text = getString(
                R.string.tp_route_name,
                it.routeData.origin!!.toUpperCase(),
                it.routeData.destination!!.toUpperCase()
            )
            et_sd_type.text = it.routeData.type

            if ((currentFrequency.sum() == 2) and (currentFrequency[0] == 1)) {
                for (t in it.tripData) {
                    if (t.day == Calendar.SUNDAY) {
                        itemList.add(
                            SelectTripDayModel(
                                R.string.sunday,
                                R.string.et_select_single_day,
                                getString(R.string.et_sd_trips, t.trips?.size),
                                R.id.et_fq_select_specific,
                                mutableListOf(1, 0, 0, 0, 0, 0, 0),
                                t.trips!!
                            )
                        )
                    } else {
                        itemList.add(
                            SelectTripDayModel(
                                R.string.et_select_weekday,
                                R.string.et_select_weekday_details,
                                getString(R.string.et_sd_trips, t.trips?.size),
                                R.id.et_fq_mon_sat,
                                mutableListOf(0, 1, 1, 1, 1, 1, 1),
                                t.trips!!
                            )
                        )
                    }
                }
            } else {
                for (t in it.tripData) {
                    val e = mutableListOf(0, 0, 0, 0, 0, 0, 0)
                    e[dayList.indexOf(t.day)] = 1
                    itemList.add(
                        SelectTripDayModel(
                            stringList[dayList.indexOf(t.day)],
                            R.string.et_select_single_day,
                            getString(R.string.et_sd_trips, t.trips?.size),
                            R.id.et_fq_select_specific,
                            e,
                            t.trips!!
                        )
                    )
                }
            }
        }
        adapter.notifyDataSetChanged()
        callback?.hideProgress()
    }

    override fun daySelected(dayModel: SelectTripDayModel) {
        sharedModel.setFrequencyData(dayModel.freq)
        sharedModel.setFrequency(dayModel.id)
        sharedModel.updateTrips(dayModel.trips)
        callback?.navigate(Constants.EDIT_START_EDITING)
    }
}
