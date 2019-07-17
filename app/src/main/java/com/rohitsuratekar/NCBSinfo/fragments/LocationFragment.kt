package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.LocationAdapter
import com.rohitsuratekar.NCBSinfo.models.Location
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.getLocations
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : MyFragment() {

    private var locationList = mutableListOf<Location>()
    private lateinit var adapter: LocationAdapter
    private var currentSort = 0
    private lateinit var iconList: ArrayList<ImageView>
    private lateinit var textList: ArrayList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iconList = arrayListOf(loc_img_name, loc_img_building, loc_img_floor, loc_img_whatever)
        textList = arrayListOf(loc_txt_name, loc_txt_build, loc_txt_floor, loc_txt_whatever)

        adapter = LocationAdapter(locationList)
        loc_recycler.adapter = adapter
        loc_recycler.layoutManager = LinearLayoutManager(context)
        loc_img_name.setOnClickListener { sortByName() }
        loc_txt_name.setOnClickListener { sortByName() }
        loc_img_floor.setOnClickListener { sortByFloor() }
        loc_txt_floor.setOnClickListener { sortByFloor() }
        loc_img_building.setOnClickListener { sortByBuilding() }
        loc_txt_build.setOnClickListener { sortByBuilding() }
        loc_img_whatever.setOnClickListener { randomize() }
        loc_txt_whatever.setOnClickListener { randomize() }

        currentSort = repository.prefs().locationSort()
        sort()
    }

    private fun sortByName() {
        currentSort = if (currentSort == 0) {
            1
        } else {
            0
        }
        sort()
    }

    private fun sortByFloor() {
        currentSort = if (currentSort == 4) {
            5
        } else {
            4
        }
        sort()
    }

    private fun sortByBuilding() {
        currentSort = if (currentSort == 2) {
            3
        } else {
            2
        }
        sort()
    }

    private fun randomize() {
        currentSort = 6
        sort()
    }

    private fun sort() {
        locationList.clear()
        locationList.addAll(getLocations())
        when (currentSort) {
            0 -> {
                locationList.sortBy { it.name }
            }
            1 -> {
                locationList.sortBy { it.name }
                locationList.reverse()
            }
            2 -> {
                locationList.sortBy { it.building }
            }
            3 -> {
                locationList.sortBy { it.building }
                locationList.reverse()
            }
            4 -> {
                locationList.sortBy { it.floor }
            }
            5 -> {
                locationList.sortBy { it.floor }
                locationList.reverse()
            }
            6 -> {
                locationList.shuffle()
            }
            else -> {
                locationList.sortBy { it.name }
            }

        }
        repository.prefs().locationSort(currentSort)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun updateUI() {
        for (img in iconList) {
            img.setColorFilter(ContextCompat.getColor(context!!, R.color.colorLight))
            img.alpha = 0.8f
        }
        for (txt in textList) {
            txt.setTextColor(ContextCompat.getColor(context!!, R.color.colorLight))
            txt.alpha = 0.8f
        }

        val changeIndex = when (currentSort) {
            2, 3 -> 1
            4, 5 -> 2
            6 -> 3
            else -> 0
        }

        iconList[changeIndex].setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        iconList[changeIndex].alpha = 1f
        textList[changeIndex].setTextColor(ContextCompat.getColor(context!!, R.color.white))
        textList[changeIndex].alpha = 1f
    }
}
