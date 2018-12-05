package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.rohitsuratekar.NCBSinfo.MainActivity
import com.rohitsuratekar.NCBSinfo.di.Repository

abstract class MyFragment : Fragment() {

    var callback: MainCallbacks? = null
    private lateinit var repository: Repository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainCallbacks) {
            callback = context
            repository = (activity as MainActivity).repository
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