package com.rohitsuratekar.NCBSinfo.models

import android.util.SparseArray
import com.rohitsuratekar.NCBSinfo.common.timeToDate
import com.rohitsuratekar.NCBSinfo.database.TripData
import java.text.ParseException
import java.util.*


class NextTrip {

    private var weekMap: SparseArray<List<String>>? = null
    private var defaultTrips: List<String>? = null

    /**
     * WeekMap will contains trips for any day of week. Keys are equal to Calender.DAY_OF_WEEK
     * of respective days. Default trips should be in following priority
     * Monday > Tuesday > .... > Sunday
     */
    constructor(weekMap: SparseArray<List<String>>) {
        this.weekMap = weekMap
        initialize()

    }

    constructor(data: List<TripData>) {
        this.weekMap = SparseArray()
        for (t in data) {
            weekMap!!.put(t.day, t.trips)
        }
        initialize()
    }

    private fun initialize() {
        //Get default trips in given priority
        for (i in Calendar.MONDAY..Calendar.SATURDAY) {
            if (weekMap!!.get(i) != null) {
                defaultTrips = weekMap!!.get(i)
                break
            }
        }

        //In Case only Sunday Trips are available
        if (defaultTrips == null) {
            defaultTrips = weekMap!!.get(Calendar.SUNDAY)
        }
    }

    /**
     * This method should return next transport trip for given timing
     *
     * @param inputCalender : User defined time
     */
    @Throws(ParseException::class)
    fun calculate(inputCalender: Calendar): Trip {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = inputCalender.timeInMillis
        val now = Date(calendar.timeInMillis) //Record input time

        //Check with trips for post midnight from yesterday
        calendar.add(Calendar.DATE, -1) //Yesterday
        val beforeFirstTrip = TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getTomorrow()
        for (s in beforeFirstTrip) {
            val d1 = timeToDate(now, s)
            if (d1.after(now) || d1 == now) {
                return Trip(s, -1, inputCalender)
            }
        }


        //Check today's trips
        calendar.add(Calendar.DATE, 1) //Today
        val todaysTrip = TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getToday()

        for (s in todaysTrip) {
            val d1 = timeToDate(now, s)
            if (d1.after(now) || d1 == now) {
                return Trip(s, 0, inputCalender)
            }
        }


        //Check tomorrow's trips (i.e. post midnight today)
        val tomorrowTrip = TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getTomorrow()
        for (s in tomorrowTrip) {
            val d1 = timeToDate(now, s)
            //In this case we need to check if time is between last trip of today and first trip of tomorrow
            //Hence we should use before instead after for comparing
            if (d1.before(now) || d1 == now) {
                return Trip(s, 1, inputCalender)
            }
        }

        //Else get first trip of tomorrow
        calendar.add(Calendar.DATE, 1)
        return Trip(getTrips(calendar.get(Calendar.DAY_OF_WEEK))[0], 2, inputCalender)
    }

    private fun getTrips(day: Int): List<String> {
        return weekMap!!.get(day) ?: kotlin.run { return defaultTrips!! }
    }


}