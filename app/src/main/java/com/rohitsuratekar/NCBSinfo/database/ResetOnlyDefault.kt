package com.rohitsuratekar.NCBSinfo.database

import android.os.AsyncTask
import android.util.Log
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.convertToList
import com.rohitsuratekar.NCBSinfo.common.serverTimestamp
import com.rohitsuratekar.NCBSinfo.di.Repository
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ResetOnlyDefault"

class ResetOnlyDefault(private val repository: Repository, private val listener: OnFinishReset) :
    AsyncTask<Void?, Void?, Void?>() {

    private val creationDate = Calendar.getInstance()
    private val modifiedDate = Calendar.getInstance()

    override fun doInBackground(vararg p0: Void?): Void? {

        val creationString = "2018-07-21 00:00:00"
        val modifiedString = "2019-07-17 00:00:00"
        val readableFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        creationDate.timeInMillis = readableFormat.parse(creationString)?.time!!
        modifiedDate.timeInMillis = readableFormat.parse(modifiedString)?.time!!

        for (info: InfoHolder in getRoutes()) {
            reset(info)
        }

        listener.resetFinished()
        return null
    }


    private fun reset(info: InfoHolder) {
        Log.i(TAG, "Reset for ${info.origin}-${info.destination} ${info.typeString} started")
        deleteRoute(info)
        handleReturn(info)
    }

    private fun deleteRoute(info: InfoHolder) {
        val routeNo: Int =
            repository.data().isRouteThere(info.origin, info.destination, info.typeString)
        if (routeNo != 0) {
            repository.data().deleteRoute(repository.data().getRouteByNumber(routeNo))
        }

        if (info.reverseAvailable) {
            val returnNo: Int =
                repository.data().isRouteThere(info.destination, info.origin, info.typeString)
            if (returnNo != 0) {
                repository.data().deleteRoute(repository.data().getRouteByNumber(returnNo))
            }
        }

        Log.i(TAG, "Old routes deleted")
    }

    private fun makeTrips(routeNo: Int, day: Int, trips: Int) {
        val t = TripData().apply {
            this.day = day
            this.routeID = routeNo
            this.trips = convertToList(repository.app().getString(trips))
        }
        repository.data().addTrips(t)
        Log.i(TAG, "Trips Updated")
    }

    private fun handleReturn(info: InfoHolder) {

        val r1 = makeRoute(info.origin, info.destination, info.typeString)
        if (info.weekDay != 0) {
            makeTrips(r1.toInt(), Calendar.MONDAY, info.weekDay)
        }
        if (info.weekEnd != 0) {
            makeTrips(r1.toInt(), Calendar.SUNDAY, info.weekEnd)
        }

        if (info.reverseAvailable) {
            val r2 = makeRoute(info.destination, info.origin, info.typeString)
            if (info.returnWeek != 0) {
                makeTrips(r2.toInt(), Calendar.MONDAY, info.returnWeek)
            }
            if (info.returnWeekEnd != 0) {
                makeTrips(r2.toInt(), Calendar.SUNDAY, info.returnWeekEnd)
            }
        }

    }


    private fun makeRoute(ori: String, des: String, typeStr: String): Long {
        val r = RouteData().apply {
            origin = ori
            destination = des
            type = typeStr
            createdOn = creationDate.serverTimestamp()
            modifiedOn = modifiedDate.serverTimestamp()
            syncedOn = modifiedDate.serverTimestamp()
            favorite = "no"
            author = Constants.DEFAULT_AUTHOR
        }
        return repository.data().addRoute(r)
    }

    private fun getRoutes(): List<InfoHolder> {
        val data = mutableListOf<InfoHolder>()

        // NCBS-IISc Shuttle
        data.add(
            InfoHolder(
                "ncbs",
                "iisc",
                "shuttle",
                R.string.def_ncbs_iisc_week,
                R.string.def_ncbs_iisc_sunday,
                R.string.def_iisc_ncbs_week,
                R.string.def_iisc_ncbs_sunday,
                true
            )
        )

        // NCBS-Mandara Shuttle
        data.add(
            InfoHolder(
                "ncbs",
                "mandara",
                "shuttle",
                R.string.def_ncbs_mandara_week,
                R.string.def_ncbs_mandara_sunday,
                R.string.def_mandara_ncbs_week,
                R.string.def_mandara_ncbs_sunday,
                true
            )
        )

        // NCBS-Mandara Buggy
        data.add(
            InfoHolder(
                "ncbs",
                "mandara",
                "buggy",
                R.string.def_buggy_from_ncbs,
                0,
                R.string.def_buggy_from_mandara,
                0,
                true
            )
        )

        // NCBS-ICTS Shuttle
        data.add(
            InfoHolder(
                "ncbs",
                "icts",
                "shuttle",
                R.string.def_ncbs_icts_week,
                R.string.def_ncbs_icts_sunday,
                R.string.def_icts_ncbs_week,
                R.string.def_icts_ncbs_sunday,
                true
            )
        )

        // NCBS-CBL
        data.add(
            InfoHolder(
                "ncbs",
                "cbl",
                "ttc",
                R.string.def_ncbs_cbl,
                0,
                0,
                0,
                false
            )
        )

        return data
    }


    data class InfoHolder(
        val origin: String,
        val destination: String,
        val typeString: String,
        val weekDay: Int,
        val weekEnd: Int,
        val returnWeek: Int,
        val returnWeekEnd: Int,
        val reverseAvailable: Boolean
    )


}

interface OnFinishReset {
    fun resetFinished()
}