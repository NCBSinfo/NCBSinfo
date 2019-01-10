package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.models.EditFragment
import kotlinx.android.synthetic.main.fragment_add_frequency.*

class AddFrequencyFragment : EditFragment(), RadioGroup.OnCheckedChangeListener,
    CompoundButton.OnCheckedChangeListener {


    private val checkBoxList = mutableListOf<CheckBox>()
    private val frequencyDetails = mutableListOf(0, 0, 0, 0, 0, 0, 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_frequency, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback?.hideProgress()
        sharedModel.updateReadState(Constants.EDIT_FREQUENCY)
        callback?.setFragmentTitle(R.string.et_add_frequency)
        et_fq_next.setOnClickListener { callback?.navigate(Constants.EDIT_TRIPS) }
        et_fq_previous.setOnClickListener { callback?.navigateWithPopback() }
        checkBoxList.clear()
        checkBoxList.addAll(
            arrayOf(
                et_fq_sunday,
                et_fq_monday,
                et_fq_tuesday,
                et_fq_wednesday,
                et_fq_thursday,
                et_fq_friday,
                et_fq_saturday
            )
        )
        et_fq_group.setOnCheckedChangeListener(this)
        et_fq_group.check(R.id.et_fq_select_specific)
        for (c in checkBoxList) {
            c.setOnCheckedChangeListener(this)
        }
        checkOldData()

    }

    private fun checkOldData() {
        sharedModel.frequencyDetails.value?.let {
            for (i in 0 until it.size) {
                frequencyDetails[i] = it[i]
                checkBoxList[i].isChecked = it[i] == 1
            }
        }
        sharedModel.transportFrequency.value?.let {
            et_fq_group.check(it)
        }

    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        group?.let {
            sharedModel.setFrequency(checkedId)
            for (c in checkBoxList) {
                c.isEnabled = false
            }
            when (checkedId) {
                R.id.et_fq_all_days -> {
                    for (c in checkBoxList) {
                        c.isChecked = true
                    }
                }
                R.id.et_fq_mon_sat -> {
                    for (c in checkBoxList) {
                        c.isChecked = c.id != R.id.et_fq_sunday
                    }
                }
                R.id.et_fq_sat_sun -> {
                    for (c in checkBoxList) {
                        c.isChecked = (c.id == R.id.et_fq_saturday) or (c.id == R.id.et_fq_sunday)
                    }
                }
                else -> {
                    sharedModel.frequencyDetails.value?.let { data ->
                        for (i in 0 until data.size) {
                            checkBoxList[i].isChecked = data[i] == 1
                            checkBoxList[i].isEnabled = true
                        }
                    } ?: kotlin.run {
                        for (c in checkBoxList) {
                            c.isChecked = false
                            c.isEnabled = true
                        }
                    }

                }
            }
        }
    }

    private fun setFrequencyDetails() {
        for (i in 0 until checkBoxList.size) {
            if (checkBoxList[i].isChecked) {
                frequencyDetails[i] = 1
            } else {
                frequencyDetails[i] = 0
            }
        }
        sharedModel.setFrequencyData(frequencyDetails)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        buttonView?.let { setFrequencyDetails() }
    }

}
