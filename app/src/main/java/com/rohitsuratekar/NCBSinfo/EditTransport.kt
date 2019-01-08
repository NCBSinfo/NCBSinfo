package com.rohitsuratekar.NCBSinfo

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportStepAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.EditCallbacks
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.di.*
import com.rohitsuratekar.NCBSinfo.models.EditTransportStep
import com.rohitsuratekar.NCBSinfo.viewmodels.EditTransportViewModel
import kotlinx.android.synthetic.main.activity_edit_transport.*
import javax.inject.Inject

class EditTransport : AppCompatActivity(), EditCallbacks, EditTransportStepAdapter.OnStepClick {


    @Inject
    lateinit var repository: Repository
    private lateinit var navController: NavController
    lateinit var sharedViewModel: EditTransportViewModel
    lateinit var adapter: EditTransportStepAdapter
    private val stepList = mutableListOf<EditTransportStep>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transport)
        navController = Navigation.findNavController(this, R.id.nav_host_transport)
        sharedViewModel = ViewModelProviders.of(this).get(EditTransportViewModel::class.java)
        transport_toolbar.setTitle(R.string.edit_transport)
        et_step_title.text = getString(R.string.et_step_title, 1)
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .prefModule(PrefModule(baseContext))
            .roomModule(RoomModule(application))
            .build()
            .inject(this)

        setupRecycler()
        subscribe()
    }

    private fun subscribe() {
        sharedViewModel.stepUpdate.observe(this, Observer {
            stepList.clear()
            stepList.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupSteps() {

        val textList =
            arrayListOf<Int>(R.string.et_name, R.string.et_type, R.string.et_trips, R.string.et_adjust, R.string.et_confirm)

        for (i in 0 until textList.size) {
            stepList.add(EditTransportStep().apply {
                number = i
                text = textList[i]
            })
        }
        stepList[0].isSeen = true
    }

    private fun setupRecycler() {
        setupSteps()
        adapter = EditTransportStepAdapter(stepList, this)
        et_step_recycler.adapter = adapter
        et_step_recycler.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        sharedViewModel.addSteps(stepList)
    }

    override fun showProgress() {
        progress_frame_transport.showMe()
    }

    override fun hideProgress() {
        progress_frame_transport.hideMe()
    }

    override fun showError(message: String) {
        hideProgress()
        AlertDialog.Builder(this@EditTransport)
            .setTitle(R.string.oops)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .show()
    }

    override fun navigate(option: Int) {
        when (option) {
            Constants.EDIT_NAME -> navController.navigate(R.id.addRouteNamesFragment)
            Constants.EDIT_TYPE -> navController.navigate(R.id.addTransportTypeFragment)
            Constants.EDIT_TRIPS -> navController.navigate(R.id.addTripsFragment)
            Constants.EDIT_START_TRIP -> navController.navigate(R.id.adjustTripFragment)
            Constants.EDIT_CONFIRM -> navController.navigate(R.id.confirmEditFragment)
        }
    }

    override fun setClicked(step: Int) {
        et_step_title.text = getString(R.string.et_step_title, step + 1)
        when (step) {
            0 -> navigate(Constants.EDIT_NAME)
            1 -> navigate(Constants.EDIT_TYPE)
            2 -> navigate(Constants.EDIT_TRIPS)
            3 -> navigate(Constants.EDIT_START_TRIP)
            4 -> navigate(Constants.EDIT_CONFIRM)
        }
    }

}
