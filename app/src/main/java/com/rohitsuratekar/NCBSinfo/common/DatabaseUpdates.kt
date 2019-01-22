package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.models.DataUpdateModel
import java.util.*


// Test function to update or modify database without disturbing user preference
fun testUpdate1(context: Context): List<DataUpdateModel> {

    val tripList = mutableListOf<TripData>()
    tripList.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips = convertToList(context.getString(R.string.def_ncbs_iisc_week))
        }
    )

    val list = mutableListOf<DataUpdateModel>()
    list.add(
        DataUpdateModel(
            "ncbs",
            "iisc",
            "shuttle",
            false,
            false,
            tripList,
            false
        )
    )
    return list
}