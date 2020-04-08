package com.rohitsuratekar.NCBSinfo.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.serverTimestamp
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.di.Repository
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "ConfirmTransport"

class ConfirmTransportViewModel : ViewModel() {

    val error = MutableLiveData<Int>()
    val success = MutableLiveData<Boolean>()


    fun addTransport(
        repository: Repository,
        origin: String,
        destination: String,
        type: String,
        frequency: List<Int>,
        trips: List<String>,
        force: Boolean
    ) {
        AddNewTransport(repository, origin, destination, type, frequency, trips, object : OnNewRoute {
            override fun conflict(int: Int) {
                error.postValue(int)
            }

            override fun routeAdded() {
                success.postValue(true)
            }

        }, force).execute()
    }

    class AddNewTransport(
        private val repository: Repository, private val origin: String,
        private val destination: String,
        private val type: String,
        private val frequency: List<Int>,
        private val inputTrips: List<String>, private val listener: OnNewRoute,
        private val force: Boolean
    ) :
        AsyncTask<Void?, Void?, Void?>() {

        private val timestamp = Calendar.getInstance().serverTimestamp()

        override fun doInBackground(vararg params: Void?): Void? {
            if ((repository.data().isRouteThere(origin, destination, type) == 0) or force) {
                when {
                    frequency.sum() == 7 -> allDays()
                    (frequency.sum() == 6) and (frequency[0] == 0) -> weekDays()
                    (frequency.sum() == 2) and (frequency[0] == 1) and (frequency[6] == 1) -> weekEnd()
                    else -> specific()
                }

            } else {
                listener.conflict(Constants.EDIT_ERROR_EXISTING_ROUTE)
            }
            return null
        }

        private fun checkOrCreateRoute(): RouteData {
            val routeNo = repository.data().isRouteThere(origin, destination, type)
            return if (routeNo == 0) {
                val r = RouteData()
                r.origin = origin
                r.destination = destination
                r.type = type
                r.author = "User"
                r.createdOn = timestamp
                r.modifiedOn = timestamp
                r.syncedOn = timestamp
                r.favorite = "no"
                val newNo = repository.data().addRoute(r)
                Log.i(TAG, "New route is added")
                repository.data().getRouteByNumber(newNo.toInt())
            } else {
                val r = repository.data().getRouteByNumber(routeNo)
                repository.data().updateModifiedDate(r, timestamp)
                r
            }
        }

        private fun allDays() {
            val r = checkOrCreateRoute()
            repository.data().deleteTripsByRoute(r.routeID)
            val t = TripData().apply {
                routeID = r.routeID
                day = Calendar.MONDAY
            }
            t.trips = inputTrips
            repository.data().addTrips(t)
            Log.i(TAG, "Route trips for ALL DAYS are updated")
            listener.routeAdded()
        }

        private fun weekDays() {
            val r = checkOrCreateRoute()
            val tripList = repository.data().getTrips(r)
            if (tripList.size == 1) {
                Log.i(TAG, "Keeping Old trip for Sunday.")
                val newTrip = TripData().apply {
                    routeID = r.routeID
                    day = Calendar.SUNDAY
                    trips = tripList[0].trips
                }
                repository.data().addTrips(newTrip)
            } else {
                for (t in tripList) {
                    if (t.day != Calendar.SUNDAY) {
                        repository.data().deleteTrip(t)
                        Log.i(TAG, "Old trip for week day deleted.")
                    }
                }
            }

            val weekDayTrip = TripData().apply {
                routeID = r.routeID
                day = Calendar.MONDAY
            }

            weekDayTrip.trips = inputTrips
            repository.data().addTrips(weekDayTrip)
            Log.i(TAG, "WEEK DAYS trips added.")
            listener.routeAdded()
        }

        private fun weekEnd() {
            val r = checkOrCreateRoute()
            val tripList = repository.data().getTrips(r)
            for (t in tripList) {
                if ((t.day == Calendar.SUNDAY) or (t.day == Calendar.SATURDAY)) {
                    repository.data().deleteTrip(t)
                    Log.i(TAG, "Old trip for weekend deleted.")
                }
            }

            val newTrip = TripData().apply {
                routeID = r.routeID
                day = Calendar.SUNDAY
                trips = inputTrips
            }
            repository.data().addTrips(newTrip)
            Log.i(TAG, "New Sunday trips added")
            newTrip.day = Calendar.SATURDAY
            repository.data().addTrips(newTrip)
            Log.i(TAG, "New Saturday trips added")
            listener.routeAdded()

        }


        private fun specific() {
            val dayList = mutableListOf(
                Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
                Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
            )
            val r = checkOrCreateRoute()
            val tripList = repository.data().getTrips(r)
            val map = HashMap<Int, TripData>()
            for (t in tripList) {
                map[t.day] = t
            }
            for (i in frequency.indices) {
                if (frequency[i] == 1) {
                    if (map.containsKey(dayList[i])) {
                        repository.data().deleteTrip(map[dayList[i]]!!)
                        Log.i(TAG, "Old day trip deleted")
                    }
                }
            }

            val newTrip = TripData().apply {
                routeID = r.routeID
                trips = inputTrips
            }

            for (i in frequency.indices) {
                if (frequency[i] == 1) {
                    newTrip.day = dayList[i]
                    repository.data().addTrips(newTrip)
                    Log.i(TAG, "New day trip added")
                }
            }
            listener.routeAdded()
        }
    }

    interface OnNewRoute {
        fun conflict(int: Int)
        fun routeAdded()
    }
}