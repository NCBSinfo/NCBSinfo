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
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .prefModule(PrefModule(baseContext))
            .roomModule(RoomModule(application))
            .build()
            .inject(this)


        setupRecycler()
        subscribe()
        et_step_recycler.hideMe()

        intent.extras?.let {
            val arg = EditTransportArgs.fromBundle(it).routeNo
            sharedViewModel.setInputRouteID(arg)
        }
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
                    } else {
                        et_fragment_subtitle.text = getString(R.string.edit_transport)
                    }
                } ?: kotlin.run {
                    et_fragment_subtitle.text = getString(R.string.create_new)
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
