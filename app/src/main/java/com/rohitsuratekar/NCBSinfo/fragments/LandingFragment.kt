package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.viewmodels.LandingViewModel
import kotlinx.android.synthetic.main.fragment_landing.*

class LandingFragment : MyFragment() {

    private lateinit var viewModel: LandingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LandingViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        viewModel.checkAvailableRoutes(repository)
    }

    private fun subscribe() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            landing_status_note.text = it
        })

        viewModel.dataLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.checkDataUpdate(repository)
            }
        })

        viewModel.dataUpdated.observe(viewLifecycleOwner, Observer {
            if (it) {
                callback?.navigate(Constants.NAVIGATE_HOME)
            }
        })
    }
}
