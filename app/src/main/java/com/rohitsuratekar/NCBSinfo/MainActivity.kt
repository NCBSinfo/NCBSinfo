package com.rohitsuratekar.NCBSinfo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rohitsuratekar.NCBSinfo.adapters.ContactDetailsAdapter
import com.rohitsuratekar.NCBSinfo.common.*
import com.rohitsuratekar.NCBSinfo.di.*
import com.rohitsuratekar.NCBSinfo.fragments.ContactDetailsFragment
import com.rohitsuratekar.NCBSinfo.fragments.TransportRoutesFragment
import com.rohitsuratekar.NCBSinfo.models.Contact
import com.rohitsuratekar.NCBSinfo.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainCallbacks, ContactDetailsAdapter.OnCalled {

    val CALL_PERMISSION = 1989 // Should NOT be private

    @Inject
    lateinit var repository: Repository
    private lateinit var navController: NavController
    lateinit var sharedViewModel: SharedViewModel
    private val permissions = arrayOf(Manifest.permission.CALL_PHONE)
    private var tempNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host)
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

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

        // Following code is needed to change start destination
        // https://stackoverflow.com/questions/51929290/
        // is-it-possible-to-set-startdestination-conditionally-using-android-navigation-ar

        val navHostFragment = nav_host as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)
        graph.setDefaultArguments(intent.extras)
        graph.startDestination = R.id.homeFragment
        navHostFragment.navController.graph = graph

        bottom_navigation.setupWithNavController(navController)

        navController.popBackStack()
        navController.navigate(R.id.homeFragment)

        for (item in bottom_navigation.menu.children) {
            item.isEnabled = true
        }
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
            Constants.NAVIGATE_TIMETABLE -> navController.navigate(R.id.timetableFragment)
        }
    }


    override fun showRouteList(currentRoute: Int) {
        val sheet = TransportRoutesFragment()
        val args = Bundle()
        args.putInt(Constants.BOTTOM_SHEET_ROUTE_ID, currentRoute)
        sheet.arguments = args
        sheet.show(supportFragmentManager, sheet.tag)
    }

    override fun showContact(contact: Contact) {
        val sheet = ContactDetailsFragment()
        val args = Bundle()
        args.putString(Constants.BOTTOM_SHEET_CONTACT_NAME, contact.name)
        args.putString(Constants.BOTTOM_SHEET_CONTACT_EXTENSION, contact.primaryExtension)
        args.putString(Constants.BOTTOM_SHEET_CONTACT_EXTRA, contact.otherExtensions)
        args.putString(Constants.BOTTOM_SHEET_CONTACT_DETAILS, contact.details)
        sheet.arguments = args
        sheet.show(supportFragmentManager, sheet.tag)
    }

    override fun calledNumber(number: String) {
        //Check permissions
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            call(number)
        } else {
            tempNumber = number
            showDenied()
        }
    }

    private fun showDenied() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(getString(R.string.ct_permission_needed))
            .setMessage(getString(R.string.ct_warning_permission))
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    permissions,
                    CALL_PERMISSION
                )
            }.setNegativeButton(
                android.R.string.cancel
            ) { _, _ -> }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            CALL_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call(tempNumber)
            } else {
                Log.e("Contacts", "Permission Denied")
                showDenied()
            }
        }
    }


    private fun call(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

}