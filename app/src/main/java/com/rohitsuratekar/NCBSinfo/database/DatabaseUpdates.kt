package com.rohitsuratekar.NCBSinfo.database

import android.content.Context
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.convertToList
import com.rohitsuratekar.NCBSinfo.models.DataUpdateModel
import java.text.SimpleDateFormat
import java.util.*


fun convertToServerTimeStamp(modifiedDate: String): String {
    val readableFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val serverFormat = SimpleDateFormat(Constants.FORMAT_SERVER_TIMESTAMP, Locale.ENGLISH)
    val cal =
        Calendar.getInstance().apply { timeInMillis = readableFormat.parse(modifiedDate)?.time!! }
    return serverFormat.format(Date(cal.timeInMillis))
}

// Test function to update or modify database without disturbing user preference
fun testUpdate1(context: Context): List<DataUpdateModel> {

    val tripList = mutableListOf<TripData>()
    tripList.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips =
                convertToList(context.getString(R.string.def_ncbs_iisc_week))
        }
    )

    val list = mutableListOf<DataUpdateModel>()
    list.add(
        DataUpdateModel(
            "ncbs",
            "iisc",
            "shuttle",
            replaceAll = false,
            createNew = false,
            tripList = tripList,
            deleteRoute = false,
            updateDate = convertToServerTimeStamp("2019-05-03 14:06:00")
        )
    )
    return list
}
