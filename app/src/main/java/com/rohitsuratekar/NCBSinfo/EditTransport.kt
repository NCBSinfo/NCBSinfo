package com.rohitsuratekar.NCBSinfo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportStepAdapter
import com.rohitsuratekar.NCBSinfo.common.*
import com.rohitsuratekar.NCBSinfo.di.*
import com.rohitsuratekar.NCBSinfo.models.EditTransportStep
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.viewmodels.EditTransportViewModel
import kotlinx.android.synthetic.main.activity_edit_transport.*
import javax.inject.Inject

class EditTransport : AppCompatActivity(), EditCallbacks, EditTransportStepAdapter.OnStepClick {


    @Inject
    lateinit var repository: Repository
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: EditTransportViewModel
    lateinit var adapter: EditTransportStepAdapter
    private val stepList = mutableListOf<EditTransportStep>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transport)
        navController = Navigation.findNavController(this, R.id.nav_host_transport)
        sharedViewModel = ViewModelProviders.of(this).get(EditTransportViewModel::class.java)
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .prefModule(PrefModule(baseContext))
            .roomModule(RoomModule(application))
            .build()
            .inject(this)


        setupRecycler()
        subscribe()
        et_step_recycler.hideMe()
        et_delete.setOnClickListener { deleteRoute() }
        et_delete.hideMe()

        intent.extras?.let {
            val arg = EditTransportArgs.fromBundle(it).routeNo
            sharedViewModel.setInputRouteID(arg)
        }
    }

    fun checkSharedModel(): EditTransportViewModel {
        if (!this::sharedViewModel.isInitialized) {
            sharedViewModel = ViewModelProviders.of(this).get(EditTransportViewModel::class.java)
        }
        return sharedViewModel
    }

    fun checkRepository(): Repository {
        if (!this::repository.isInitialized) {
            DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .prefModule(PrefModule(baseContext))
                .roomModule(RoomModule(application))
                .build()
                .inject(this)
        }
        return repository
    }


    private fun subscribe() {
        sharedViewModel.stepUpdate.observe(this, Observer {
            stepList.clear()
            stepList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        sharedViewModel.routeDeleted.observe(this, Observer {
            if (it) {
                baseContext.toast("Route Deleted")
                navigateToHome()
            }
        })
    }

    private fun deleteSingleTrip(route: Route) {
        sharedViewModel.selectedTrip.value?.let {
            AlertDialog.Builder(this@EditTransport)
                .setTitle(R.string.warning)
                .setMessage(
                    getString(
                        R.string.et_delete_warning,
                        route.tripData.size,
                        route.routeData.origin?.toUpperCase(),
                        route.routeData.destination?.toUpperCase(),
                        route.routeData.type
                    )
                )
                .setPositiveButton(R.string.et_continue) { _, _ ->
                    showProgress()
                    sharedViewModel.deleteRoute(repository, route, it)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        } ?: kotlin.run {
            showError(getString(R.string.et_delete_error))
        }

    }

    private fun deleteRoute() {
        sharedViewModel.currentRoute.value?.let {
            if (it.tripData.size > 1) {
                deleteSingleTrip(it)
            } else {
                AlertDialog.Builder(this@EditTransport)
                    .setTitle(R.string.warning)
                    .setMessage(
                        getString(
                            R.string.et_delete_only_trip,
                            it.routeData.origin?.toUpperCase(),
                            it.routeData.destination?.toUpperCase(),
                            it.routeData.type
                        )
                    )
                    .setPositiveButton(R.string.et_continue) { _, _ ->
                        showProgress()
                        sharedViewModel.deleteRoute(repository, it, null)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
        } ?: kotlin.run {
            showError(getString(R.string.et_delete_error))
        }

    }

    private fun setupSteps() {

        val textList =
            arrayListOf(
                R.string.et_name,
                R.string.et_type,
                R.string.et_frequency,
                R.string.et_trips,
                R.string.et_adjust,
                R.string.et_confirm
            )

        for (i in 0 until textList.size) {
            stepList.add(EditTransportStep().apply {
                number = i
                text = textList[i]
            })
        }
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
            Constants.EDIT_FREQUENCY -> navController.navigate(R.id.addFrequencyFragment)
            Constants.EDIT_FINISH -> finish()
            Constants.EDIT_START_EDITING -> {
                navController.popBackStack()
                navController.navigate(R.id.addRouteNamesFragment)
                et_step_recycler.showMe()
                sharedViewModel.inputRouteID.value?.let {
                    if (it == -1) {
                        et_fragment_subtitle.text = getString(R.string.create_new)
                        et_delete.hideMe()
                    } else {
                        et_fragment_subtitle.text = getString(R.string.edit_transport)
                        et_delete.showMe()
                    }
                } ?: kotlin.run {
                    et_fragment_subtitle.text = getString(R.string.create_new)
                    et_delete.hideMe()
                }
            }

        }
    }

    override fun navigateWithPopback() {
        navController.popBackStack()
    }

    override fun setClicked(step: Int) {
        navigate(step)
    }

    override fun setFragmentTitle(id: Int) {
        et_fragment_title.text = getString(id)
    }

    override fun navigateToHome() {
        val intent = Intent(this@EditTransport, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
