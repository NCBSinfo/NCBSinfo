package com.rohitsuratekar.NCBSinfo.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.common.toast
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.NextTrip
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : MyFragment() {

    private lateinit var viewModel: HomeViewModel
    private val routeList = mutableListOf<RouteData>()
    private var currentRoute: Route? = null
    private val currentCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        viewModel.getRouteList(repository)
        viewModel.getFavorite(repository)

        hm_fav.setOnClickListener {
            currentRoute?.routeData?.let { r ->
                callback?.showProgress()
                context?.toast("Favorite Route Changed!")
                if (currentRoute?.routeData?.favorite == "yes") {
                    viewModel.changeFavorite(r, false, repository)
                } else {
                    viewModel.changeFavorite(r, true, repository)
                }
            }

        }

        hm_change_route.setOnClickListener {
            currentRoute?.routeData?.routeID?.let { routeID ->
                callback?.showRouteList(routeID)
            }
        }

        hm_see_all.setOnClickListener {
            callback?.navigate(Constants.NAVIGATE_TIMETABLE)
        }

        hm_covid_who.setOnClickListener {
            gotoGOI()
        }

        hm_covid_ncbs.setOnClickListener {
            gotoNCBI()
        }

        hm_emergency.setOnClickListener {
            gotoHelpline()
        }

    }

    private fun gotoGOI() {
        val currentUrl = "https://www.mohfw.gov.in/"
        val i2 = Intent(Intent.ACTION_VIEW)
        i2.data = Uri.parse(currentUrl)
        startActivity(i2)
    }

    private fun gotoNCBI() {
        val currentUrl =
            "https://www.ncbs.res.in/content/blisc-ncbs-instem-c-camp-responses-covid-19"
        val i2 = Intent(Intent.ACTION_VIEW)
        i2.data = Uri.parse(currentUrl)
        startActivity(i2)
    }

    private fun gotoHelpline() {
        val currentUrl =
            "https://www.mohfw.gov.in/pdf/coronvavirushelplinenumber.pdf"
        val i2 = Intent(Intent.ACTION_VIEW)
        i2.data = Uri.parse(currentUrl)
        startActivity(i2)
    }


    private fun subscribe() {
        viewModel.routeList.observe(viewLifecycleOwner, Observer {
            routeList.clear()
            routeList.addAll(it)
        })
        viewModel.returnedRoute.observe(viewLifecycleOwner, Observer {
            if (sharedModel.currentRoute.value == null) {
                sharedModel.changeCurrentRoute(it)
            } else {
                currentRoute = it
                updateUI()
            }
        })

        sharedModel.currentRoute.observe(viewLifecycleOwner, Observer {
            changeRoute(it.routeData)
        })
    }

    private fun updateUI() {
        callback?.hideProgress()
        currentRoute?.let {
            hm_origin.text = it.routeData.origin?.toUpperCase(Locale.getDefault())
            hm_destination.text = it.routeData.destination?.toUpperCase(Locale.getDefault())
            hm_type.text = getString(R.string.hm_next, it.routeData.type)
            if (it.routeData.type == "other") {
                hm_type.text = getString(R.string.hm_next, "transport")
            }
            hm_time.text = NextTrip(it.tripData).calculate(currentCalendar).displayTime()
            if (it.routeData.favorite == "yes") {
                hm_fav.setImageResource(R.drawable.icon_fav)
                hm_fav.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAccent))
            } else {
                hm_fav.setImageResource(R.drawable.icon_fav_empty)
                hm_fav.setColorFilter(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }

            val day = NextTrip(it.tripData).calculate(currentCalendar)
            if (day.tripHighlightDay() == currentCalendar.get(Calendar.DAY_OF_WEEK)
            ) {
                hm_next_day.hideMe()
            } else {
                hm_next_day.showMe()
                hm_next_day.text = getString(
                    R.string.hm_next_day, day.tripCalender()
                        .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                )
            }
        }

    }

    private fun changeRoute(routeData: RouteData) {
        callback?.showProgress()
        viewModel.changeCurrentRoute(routeData, repository)
    }


}
