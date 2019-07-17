package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import kotlinx.android.synthetic.main.fragment_add_transport_type.*

class AddTransportTypeFragment : EditFragment(), RadioGroup.OnCheckedChangeListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transport_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_TYPE)
        callback?.setFragmentTitle(R.string.et_transport_type)
        et_type_group.setOnCheckedChangeListener(this)
        et_type_next.setOnClickListener { callback?.navigate(Constants.EDIT_FREQUENCY) }
        et_type_previous.setOnClickListener { callback?.navigateWithPopback() }
        checkForOldData()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        group?.let {
            sharedModel.setType(checkedId)
        }
    }

    private fun checkForOldData() {
        sharedModel.transportType.value?.let {
            et_type_group.check(it)
        }
    }


}
