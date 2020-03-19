package com.rohitsuratekar.NCBSinfo.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportConfirmAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.common.toast
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import com.rohitsuratekar.NCBSinfo.viewmodels.ConfirmTransportViewModel
import kotlinx.android.synthetic.main.fragment_confirm_edit.*
import java.util.*

class ConfirmEditFragment : EditFragment() {

    private var allSet = true
    private val dayList = mutableListOf<TextView>()
    private lateinit var adapter: EditTransportConfirmAdapter
    private lateinit var viewModel: ConfirmTransportViewModel
    private val tripList = mutableListOf<String>()
    private var origin: String? = null
    private var destination: String? = null
    private var type: String? = null
    private val frequency = mutableListOf<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_edit, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter =
            EditTransportConfirmAdapter(tripList, object : EditTransportConfirmAdapter.OnItemClick {
                override fun itemClicked() {
                    callback?.navigate(Constants.EDIT_TRIPS)
                }

            })
        viewModel = ViewModelProvider(this).get(ConfirmTransportViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_CONFIRM)
        callback?.setFragmentTitle(R.string.et_confirm_route)
        et_cn_previous.setOnClickListener { callback?.navigateWithPopback() }
        et_cn_confirm.setOnClickListener { startUpdate() }
        dayList.clear()
        dayList.addAll(
            arrayOf(
                et_cn_0, et_cn_1, et_cn_2, et_cn_3, et_cn_4, et_cn_5, et_cn_6
            )
        )
        et_cn_recycler.adapter = adapter
        et_cn_recycler.layoutManager = LinearLayoutManager(context)
        et_cn_name.setOnClickListener { callback?.navigate(Constants.EDIT_NAME) }
        et_cn_holder.setOnClickListener { callback?.navigate(Constants.EDIT_FREQUENCY) }
        et_cn_type.setOnClickListener { callback?.navigate(Constants.EDIT_TYPE) }
        checkIfReady()
        subscribe()
    }

    private fun subscribe() {

        viewModel.success.observe(viewLifecycleOwner, Observer {
            if (it) {
                context?.toast("Database successfully updated!")
                callback?.navigateToHome()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            releaseUI()
            if (it == Constants.EDIT_ERROR_EXISTING_ROUTE) {
                AlertDialog.Builder(context)
                    .setTitle(R.string.warning)
                    .setCancelable(false)
                    .setMessage(
                        getString(
                            R.string.et_same_route_error,
                            origin?.toUpperCase(Locale.getDefault()),
                            destination?.toUpperCase(Locale.getDefault()),
                            type
                        )
                    )
                    .setPositiveButton(R.string.et_continue) { _, _ ->
                        blockUI()
                        viewModel.addTransport(
                            repository,
                            origin!!,
                            destination!!,
                            type!!,
                            frequency,
                            tripList,
                            true
                        )

                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
        })

    }

    private fun checkIfReady() {
        var name = ""
        sharedModel.origin.value?.let {
            name = it
            origin = it.trim().toLowerCase(Locale.getDefault())
        } ?: kotlin.run {
            allSet = false
            name = getString(R.string.et_not_set)

        }

        sharedModel.destination.value?.let {
            name += " - $it"
            destination = it.trim().toLowerCase(Locale.getDefault())
        } ?: kotlin.run {
            allSet = false
            name += " - " + getString(R.string.et_not_set)
        }


        et_cn_name.text = name

        var tempType = getString(R.string.et_not_set)
        sharedModel.transportType.value?.let {
            when (it) {
                R.id.et_type_option1 -> tempType = "shuttle"
                R.id.et_type_option2 -> tempType = "ttc"
                R.id.et_type_option3 -> tempType = "buggy"
                R.id.et_type_option4 -> tempType = "other"
            }

            type = tempType.trim().toLowerCase(Locale.getDefault())

        } ?: kotlin.run {
            allSet = false
        }
        et_cn_type.text = type

        sharedModel.frequencyDetails.value?.let {
            for (i in 0 until dayList.size) {
                if (it[i] == 1) {
                    dayList[i].setBackgroundResource(R.color.colorPrimary)
                } else {
                    dayList[i].setBackgroundResource(R.color.extension_back)
                }
            }
            frequency.clear()
            frequency.addAll(it)
            if (it.sum() == 0) {
                allSet = false
            }
        } ?: kotlin.run {
            for (d in dayList) {
                d.setBackgroundResource(R.color.extension_back)
            }
            allSet = false
        }

        sharedModel.tripList.value?.let {
            if (it.isEmpty()) {
                et_cn_empty_trips.showMe()
                allSet = false
            } else {
                et_cn_empty_trips.hideMe()
                tripList.clear()
                tripList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        } ?: kotlin.run {
            et_cn_empty_trips.showMe()
            allSet = false
        }

        sharedModel.tripSelected.value?.let {
            if (!it) {
                allSet = false
            }
        } ?: kotlin.run {
            allSet = false
        }

        if (allSet) {
            et_cn_confirm.isEnabled = true
            et_cn_confirm.alpha = 1F
            et_cn_note.text = getString(R.string.et_confirm_note)
        } else {
            et_cn_confirm.isEnabled = false
            et_cn_confirm.alpha = 0.8F
            et_cn_note.text = getString(R.string.et_confirm_warning)
        }


    }

    private fun blockUI() {
        et_cn_confirm.isEnabled = false
        et_cn_confirm.alpha = 0.8F
        et_cn_previous.isEnabled = false
        et_cn_previous.alpha = 0.8F
        callback?.showProgress()
    }

    private fun releaseUI() {
        et_cn_confirm.isEnabled = true
        et_cn_confirm.alpha = 1F
        et_cn_previous.isEnabled = true
        et_cn_previous.alpha = 1F
        callback?.hideProgress()
    }

    private fun startUpdate() {
        blockUI()
        viewModel.addTransport(
            repository,
            origin!!,
            destination!!,
            type!!,
            frequency,
            tripList,
            false
        )
    }


}
