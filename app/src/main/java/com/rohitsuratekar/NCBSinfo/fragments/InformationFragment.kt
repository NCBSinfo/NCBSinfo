package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.InformationAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import kotlinx.android.synthetic.main.fragment_information.*


class InformationFragment : MyFragment() {

    data class Information(var title: Int, var details: Int, var image: Int, var action: Int)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = InformationAdapter(getInfoList(), object : InformationAdapter.OnInfoClick {
            override fun sectionClicked(action: Int) {
                callback?.navigate(action)
            }
        })

        info_recycler.adapter = adapter
        info_recycler.layoutManager = LinearLayoutManager(context)


    }

    private fun getInfoList(): List<Information> {
        val infoList = mutableListOf<Information>()
        infoList.add(
            Information(
                R.string.manage_transport, R.string.info_manage_transport_details,
                R.drawable.graphics_builder, Constants.NAVIGATE_MANAGE_TRANSPORT
            )
        )
        infoList.add(
            Information(
                R.string.locations,
                R.string.info_loc_details,
                R.drawable.graphics_location,
                Constants.NAVIGATE_LOCATIONS
            )
        )
        return infoList
    }
}
