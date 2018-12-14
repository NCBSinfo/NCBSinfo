package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.convertToList
import com.rohitsuratekar.NCBSinfo.common.serverTimestamp
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.di.Repository
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "LandingViewModel"

class LandingViewModel : ViewModel() {

    val status = MutableLiveData<String>()
    val dataLoaded = MutableLiveData<Boolean>()

    fun checkAvailableRoutes(repository: Repository) {

        CheckRoutes(repository, object : OnFinishRetrieving {
            override fun dataLoadFinished() {
                dataLoaded.postValue(true)
            }

            override fun changeStatus(statusNote: String) {
                status.postValue(statusNote)
            }

        }).execute()
    }

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
            val modifiedString = "2018-08-27 00:00:00"

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
                origin = originString.toLowerCase()
                destination = destinationString.toLowerCase()
                type = typeString.toLowerCase()
                createdOn = creation.serverTimestamp()
                modifiedOn = modified.serverTimestamp()
                synced = modified.serverTimestamp()
                favorite = "no"
                author = "SecretBiology"
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
    }
}