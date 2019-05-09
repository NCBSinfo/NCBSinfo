package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.models.DataUpdateModel
import java.text.SimpleDateFormat
import java.util.*


fun convertToServerTimeStamp(modifiedDate: String): String {
    val readableFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val serverFormat = SimpleDateFormat(Constants.FORMAT_SERVER_TIMESTAMP, Locale.ENGLISH)
    val cal = Calendar.getInstance().apply { timeInMillis = readableFormat.parse(modifiedDate).time }
    return serverFormat.format(Date(cal.timeInMillis))
}

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
            false,
            convertToServerTimeStamp("2019-05-03 14:06:00")
        )
    )
    return list
}

/**
 * Update based on announcement on 3 May 2019 for NCBS/IISC
 * as well as
 * Change in ICTS/NCBS on 20 March 2019
 */

fun updateMay19(context: Context): List<DataUpdateModel> {

    val tripListNCBS = mutableListOf<TripData>()
    tripListNCBS.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips = convertToList(context.getString(R.string.def_ncbs_iisc_week))
        }
    )

    val tripListIISc = mutableListOf<TripData>()
    tripListIISc.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips = convertToList(context.getString(R.string.def_iisc_ncbs_week))
        }
    )

    val tripICTSNCBS = mutableListOf<TripData>()
    tripICTSNCBS.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips = convertToList(context.getString(R.string.def_icts_ncbs_week))
        }
    )
    tripICTSNCBS.add(
        TripData().apply {
            day = Calendar.SUNDAY
            trips = convertToList(context.getString(R.string.def_icts_ncbs_sunday))
        }
    )

    val tripNCBSICTS = mutableListOf<TripData>()
    tripNCBSICTS.add(
        TripData().apply {
            day = Calendar.MONDAY
            trips = convertToList(context.getString(R.string.def_ncbs_icts_week))
        }
    )
    tripNCBSICTS.add(
        TripData().apply {
            day = Calendar.SUNDAY
            trips = convertToList(context.getString(R.string.def_ncbs_icts_sunday))
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
            tripListNCBS,
            false,
            convertToServerTimeStamp("2019-05-03 14:06:00")
        )
    )
    list.add(
        DataUpdateModel(
            "iisc",
            "ncbs",
            "shuttle",
            false,
            false,
            tripListIISc,
            false,
            convertToServerTimeStamp("2019-05-03 14:06:00")
        )
    )
    list.add(
        DataUpdateModel(
            "icts",
            "ncbs",
            "shuttle",
            false,
            false,
            tripICTSNCBS,
            false,
            convertToServerTimeStamp("2019-03-20 00:00:00")
        )
    )
    list.add(
        DataUpdateModel(
            "ncbs",
            "icts",
            "shuttle",
            false,
            false,
            tripNCBSICTS,
            false,
            convertToServerTimeStamp("2019-03-20 00:00:00")
        )
    )
    return list
}