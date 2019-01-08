package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditFragment

class ConfirmEditFragment : EditFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_CONFIRM)
        subscribe()
    }

    private fun subscribe() {

    }


}
