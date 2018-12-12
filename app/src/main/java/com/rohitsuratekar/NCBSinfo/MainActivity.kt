package com.rohitsuratekar.NCBSinfo

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.MainCallbacks
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.*
import com.rohitsuratekar.NCBSinfo.fragments.HomeFragment
import com.rohitsuratekar.NCBSinfo.fragments.TransportRoutesFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainCallbacks, TransportRoutesFragment.OnRouteChanged {


    @Inject
    lateinit var repository: Repository
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host)
        bottom_navigation.setupWithNavController(navController)

        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .prefModule(PrefModule(baseContext))
            .roomModule(RoomModule(application))
            .build()
            .inject(this)

        for (item in bottom_navigation.menu.children) {
            item.isEnabled = false
        }

    }

    private fun gotoHome() {
        for (item in bottom_navigation.menu.children) {
            item.isEnabled = true
        }
        navController.popBackStack()
        navController.navigate(R.id.homeFragment)
    }

    override fun showProgress() {
        progress_frame.showMe()
    }

    override fun hideProgress() {
        progress_frame.hideMe()
    }

    override fun showError(message: String) {
        hideProgress()
        AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.oops)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .show()
    }

    override fun navigate(option: Int) {
        when (option) {
            Constants.NAVIGATE_HOME -> gotoHome()
        }
    }


    override fun showRouteList(currentRoute: Int) {
        val sheet = TransportRoutesFragment()
        val args = Bundle()
        args.putInt(Constants.BOTTOM_SHEET_ROUTEID, currentRoute)
        sheet.arguments = args
        sheet.show(supportFragmentManager, sheet.tag)
    }

    override fun newRoute(routeData: RouteData) {

        supportFragmentManager.findFragmentById(R.id.homeFragment)?.let {
            (it as HomeFragment).changeRoute(routeData)
        } ?: kotlin.run {
            Log.i("TAG=====", "Here.....")
        }
    }
}
