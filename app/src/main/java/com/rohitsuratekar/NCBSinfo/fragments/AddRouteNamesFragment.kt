package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideKeyboard
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import kotlinx.android.synthetic.main.fragment_add_route_names.*

class AddRouteNamesFragment : EditFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_route_names, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_NAME)


        et_input_origin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    sharedModel.setOrigin(it.toString())
                    if (!et_input_destination.text.isNullOrEmpty() and it.isNotEmpty()) {
                        sharedModel.updateConfirmState(Constants.EDIT_NAME, true)
                    } else {
                        sharedModel.updateConfirmState(Constants.EDIT_NAME, false)
                    }
                }
            }
        })

        et_input_destination.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    sharedModel.setDestination(it.toString())
                    if (!et_input_origin.text.isNullOrEmpty() and it.isNotEmpty()) {
                        sharedModel.updateConfirmState(Constants.EDIT_NAME, true)
                    } else {
                        sharedModel.updateConfirmState(Constants.EDIT_NAME, false)
                    }
                }
            }

        })

        checkPreFill()
    }

    private fun checkPreFill() {

        sharedModel.origin.value?.let {
            et_input_origin.setText(it)
        }
        sharedModel.destination.value?.let {
            et_input_destination.setText(it)
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let { hideKeyboard(it) }
    }
}
