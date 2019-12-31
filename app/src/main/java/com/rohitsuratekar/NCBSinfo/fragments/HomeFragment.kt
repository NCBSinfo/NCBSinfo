package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
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
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
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

    }

    private fun subscribe() {
        viewModel.routeList.observe(this, Observer {
            routeList.clear()
            routeList.addAll(it)
        })
        viewModel.returnedRoute.observe(this, Observer {
            if (sharedModel.currentRoute.value == null) {
                sharedModel.changeCurrentRoute(it)
            } else {
                currentRoute = it
                updateUI()
            }
        })

        sharedModel.currentRoute.observe(this, Observer {
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
        }

    }

    private fun changeRoute(routeData: RouteData) {
        callback?.showProgress()
        viewModel.changeCurrentRoute(routeData, repository)
    }
}
