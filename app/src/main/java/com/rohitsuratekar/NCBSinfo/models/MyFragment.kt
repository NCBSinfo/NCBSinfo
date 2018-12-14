package com.rohitsuratekar.NCBSinfo.models

import android.content.Context
import androidx.fragment.app.Fragment
import com.rohitsuratekar.NCBSinfo.MainActivity
import com.rohitsuratekar.NCBSinfo.common.MainCallbacks
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.viewmodels.SharedViewModel

abstract class MyFragment : Fragment() {

    var callback: MainCallbacks? = null
    lateinit var repository: Repository
    lateinit var sharedModel: SharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainCallbacks) {
            callback = context
            repository = (activity as MainActivity).repository
            sharedModel = (activity as MainActivity).sharedViewModel
        } else {
            throw RuntimeException(context.toString() + " must implement MainCallbacks")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    fun title(int: Int) {
        activity?.setTitle(int)
    }
}