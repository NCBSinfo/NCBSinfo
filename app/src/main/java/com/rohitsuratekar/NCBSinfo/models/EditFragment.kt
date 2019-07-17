package com.rohitsuratekar.NCBSinfo.models

import android.content.Context
import androidx.fragment.app.Fragment
import com.rohitsuratekar.NCBSinfo.EditTransport
import com.rohitsuratekar.NCBSinfo.common.EditCallbacks
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.viewmodels.EditTransportViewModel

abstract class EditFragment : Fragment() {

    lateinit var repository: Repository
    var callback: EditCallbacks? = null
    lateinit var sharedModel: EditTransportViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditCallbacks) {
            callback = context
            repository = (activity as EditTransport).checkRepository()
            sharedModel = (activity as EditTransport).checkSharedModel()
        } else {
            throw RuntimeException(context.toString() + " must implement EditCallbacks")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}