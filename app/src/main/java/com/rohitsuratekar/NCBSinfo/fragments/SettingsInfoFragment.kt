package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.SettingsActions
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import kotlinx.android.synthetic.main.fragment_settings_info.*

class SettingsInfoFragment : MyFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            when (SettingsInfoFragmentArgs.fromBundle(it).action) {
                SettingsActions.ACTION_ABOUT_US -> {
                    setupText(R.string.about_us, R.string.settings_about)
                }
                SettingsActions.ACTION_PRIVACY -> {
                    setupText(R.string.privacy_statement, R.string.settings_privacy)
                }
                SettingsActions.ACTION_ACK -> {
                    setupText(R.string.libraries_used, R.string.settings_acknow)
                }
                SettingsActions.ACTION_TERMS -> {
                    setupText(R.string.terms, R.string.settings_terms)
                }
                SettingsActions.ACTION_NOTICE -> {
                    setupText(R.string.halted_development, R.string.bid_adieu)
                }
            }
        }
    }

    private fun setupText(id: Int, title: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            st_info_text.text = Html.fromHtml(getString(id), Html.FROM_HTML_MODE_LEGACY)
        } else {
            st_info_text.text = Html.fromHtml(getString(id))
        }
        st_info_title.setText(title)
        st_info_text.movementMethod = LinkMovementMethod.getInstance()
    }
}
