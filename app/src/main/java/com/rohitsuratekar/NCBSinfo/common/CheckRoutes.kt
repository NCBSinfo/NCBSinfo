package com.rohitsuratekar.NCBSinfo.common

import android.os.AsyncTask
import android.util.Log
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.models.Route
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CheckRoute"

class CheckRoutes(private val repository: Repository, private val listener: OnFinishRetrieving) :
    AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg params: Void?): Void? {
        val routes = repository.data().getAllRoutes()
        if (routes.isEmpty()) {
            makeDefaultRoutes()
        } else {
            listener.dataLoadFinished()
        }
        return null
    }

    private fun makeDefaultRoutes() {
        Log.i(TAG, "Making default routes.")
        listener.changeStatus(repository.app().getString(R.string.making_default))

        val creationString = "2018-07-21 00:00:00"
        val modifiedString = "2019-05-03 00:00:00"

        val readableFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        val creationDate = Calendar.getInstance()
        val modifiedDate = Calendar.getInstance()

        creationDate.timeInMillis = readableFormat.parse(creationString).time
        modifiedDate.timeInMillis = readableFormat.parse(modifiedString).time

        //NCBS - IISC Shuttle
        val r1 = makeRoute("ncbs", "iisc", "shuttle", creationDate, modifiedDate)
        makeTrips(r1, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_ncbs_iisc_week)))
        makeTrips(r1, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_ncbs_iisc_sunday)))

        // IISC - NCBS Shuttle
        val r2 = makeRoute("iisc", "ncbs", "shuttle", creationDate, modifiedDate)
        makeTrips(r2, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_iisc_ncbs_week)))
        makeTrips(r2, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_iisc_ncbs_sunday)))


        //NCBS - Mandara Shuttle
        val r3 = makeRoute("ncbs", "mandara", "shuttle", creationDate, modifiedDate)
        makeTrips(r3, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_ncbs_mandara_week)))
        makeTrips(r3, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_ncbs_mandara_sunday)))

        //Mandara - NCBS Shuttle
        val r4 = makeRoute("mandara", "ncbs", "shuttle", creationDate, modifiedDate)
        makeTrips(r4, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_mandara_ncbs_week)))
        makeTrips(r4, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_mandara_ncbs_sunday)))

        //NCBS - Mandara Buggy
        val r5 = makeRoute("ncbs", "mandara", "buggy", creationDate, modifiedDate)
        makeTrips(r5, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_buggy_from_ncbs)))

        //Mandara - NCBS Buggy
        val r6 = makeRoute("mandara", "ncbs", "buggy", creationDate, modifiedDate)
        makeTrips(r6, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_buggy_from_mandara)))


        //NCBS - ICTS Shuttle
        val r7 = makeRoute("ncbs", "icts", "shuttle", creationDate, modifiedDate)
        makeTrips(r7, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_ncbs_icts_week)))
        makeTrips(r7, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_ncbs_icts_sunday)))

        //ICTS-NCBS Shuttle
        val r8 = makeRoute("icts", "ncbs", "shuttle", creationDate, modifiedDate)
        makeTrips(r8, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_icts_ncbs_week)))
        makeTrips(r8, Calendar.SUNDAY, convertToList(repository.app().getString(R.string.def_icts_ncbs_sunday)))

        //NCBS-CBL TTC
        val r9 = makeRoute("ncbs", "cbl", "ttc", creationDate, modifiedDate)
        makeTrips(r9, Calendar.MONDAY, convertToList(repository.app().getString(R.string.def_ncbs_cbl)))


        val routeList = repository.data().getAllRoutes()
        val returnList = mutableListOf<Route>()
        for (r in routeList) {
            returnList.add(Route(r, repository.data().getTrips(r)))
        }
        listener.returnRoutes(returnList)
        // If default data is reset, no need to check database update, hence update the "UPDATE_VERSION"
        repository.prefs().updateVersion(Constants.UPDATE_VERSION)
        // Finish database loading
        listener.dataLoadFinished()
    }

    private fun makeRoute(
        originString: String,
        destinationString: String,
        typeString: String,
        creation: Calendar,
        modified: Calendar
    ): RouteData {
        val route = RouteData().apply {
            origin = originString.toLowerCase(Locale.getDefault())
            destination = destinationString.toLowerCase(Locale.getDefault())
            type = typeString.toLowerCase(Locale.getDefault())
            createdOn = creation.serverTimestamp()
            modifiedOn = modified.serverTimestamp()
            syncedOn = modified.serverTimestamp()
            favorite = "no"
            author = Constants.DEFAULT_AUTHOR
        }
        val no = repository.data().addRoute(route)
        route.routeID = no.toInt()
        Log.i(TAG, "New route $originString - $destinationString $typeString is made")
        return route
    }

    private fun makeTrips(route: RouteData, tripDay: Int, tripsList: List<String>) {
        val tripData = TripData().apply {
            routeID = route.routeID
            trips = tripsList
            day = tripDay

        }
        repository.data().addTrips(tripData)
    }

}

interface OnFinishRetrieving {
    fun dataLoadFinished()
    fun changeStatus(statusNote: String)
    fun returnRoutes(routeList: List<Route>)
}