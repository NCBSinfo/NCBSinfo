package com.rohitsuratekar.NCBSinfo.fragments


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.BuildConfig
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.SettingsAdapter
import com.rohitsuratekar.NCBSinfo.adapters.SettingsDialogAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_ABOUT_US
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_ACK
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_APP_DETAILS
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_CRASH_REPORT
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_DEFAULT_ROUTE
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_FEEDBACK
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_GITHUB
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_NOTICE
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_PRIVACY
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_RESET_ROUTE
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_TERMS
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_TRANSPORT_UPDATE
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_HEADER
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_ITEM
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_LINE
import com.rohitsuratekar.NCBSinfo.common.toast
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.models.SettingsItem
import com.rohitsuratekar.NCBSinfo.viewmodels.ManageTransportViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : MyFragment(), SettingsAdapter.OnSettingItemClicked {


    private val itemList = mutableListOf<SettingsItem>()
    private lateinit var adapter: SettingsAdapter
    private lateinit var viewModel: ManageTransportViewModel
    private var lastUpdate: String = "16 Oct 2017"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SettingsAdapter(itemList, this)
        viewModel = ViewModelProviders.of(this).get(ManageTransportViewModel::class.java)
        subscribe()
        viewModel.getLastUpdate(repository)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpItems()
        st_recycler.adapter = adapter
        st_recycler.layoutManager = LinearLayoutManager(context)

    }

    private fun subscribe() {
        viewModel.routeDeleted.observe(this, Observer {
            callback?.hideProgress()
            sharedModel.changeCurrentRoute(it[0])
            context?.toast("Route Reset Completed!")
            viewModel.getLastUpdate(repository)
        })
        viewModel.lastModified.observe(this, Observer {
            lastUpdate = it
            setUpItems()
        })
        viewModel.routeList.observe(this, Observer {
            showTransportDialog(it)
        })
        viewModel.favoriteChanged.observe(this, Observer {
            context?.toast("Default Route Changed")
        })
    }

    private fun showTransportDialog(routeList: List<Route>) {
        var favLoc = 0
        val itemList = mutableListOf<String>()
        for (r in routeList) {
            itemList.add(
                getString(
                    R.string.settings_route_name,
                    r.routeData.origin?.toUpperCase(Locale.getDefault()),
                    r.routeData.destination?.toUpperCase(Locale.getDefault()),
                    r.routeData.type
                )
            )
            if (r.isFavorite) {
                favLoc = routeList.indexOf(r)
            }
        }
        val dialog = AlertDialog.Builder(context).create()
        val dialogAdapter =
            SettingsDialogAdapter(itemList, favLoc, object : SettingsDialogAdapter.OnRouteSelected {
                override fun routeSelected(position: Int) {
                    viewModel.updateFavorite(repository, routeList[position])
                    dialog.dismiss()
                }

            })

        val view = layoutInflater.inflate(R.layout.fragment_settings_custom_dialog, null)
        view.findViewById<RecyclerView>(R.id.st_dialog_recycler).adapter = dialogAdapter
        view.findViewById<RecyclerView>(R.id.st_dialog_recycler).layoutManager =
            LinearLayoutManager(context)
        view.findViewById<Button>(R.id.st_dialog_btn).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setView(view)
        dialog.show()
    }

    private fun convertBool(value: Boolean): String {
        if (value) {
            return "ON"
        }
        return "OFF"
    }

    private fun setUpItems() {
        itemList.clear()
        for (a in items) {
            when {
                a[0] == VIEW_HEADER -> itemList.add(SettingsItem(getString(a[1])))
                a[0] == VIEW_ITEM -> itemList.add(SettingsItem(VIEW_ITEM).apply {
                    title = getString(a[1])
                    description = getString((a[2]))
                    icon = a[3]
                    action = a[4]
                    isDisabled = false
                    when (action) {
                        ACTION_TRANSPORT_UPDATE -> {
                            description = getString((a[2]), convertFormat(lastUpdate))
                            isDisabled = true
                        }
                        ACTION_APP_DETAILS -> isDisabled = true
                        ACTION_CRASH_REPORT -> description =
                            getString(a[2], convertBool(repository.prefs().crashReportingEnabled()))
                    }
                })
                else -> itemList.add(SettingsItem(VIEW_LINE))
            }
        }

        adapter.notifyDataSetChanged()

    }

    private var items = arrayOf(
        arrayOf(VIEW_HEADER, R.string.settings_header_notice),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_development_notice,
            R.string.settings_development_notice_details,
            R.drawable.icon_announcement,
            ACTION_NOTICE
        ),
        arrayOf(VIEW_LINE),
        arrayOf(VIEW_HEADER, R.string.settings_header_transport),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_default_route,
            R.string.settings_default_transport_details,
            R.drawable.icon_bus,
            ACTION_DEFAULT_ROUTE
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_reset_transport,
            R.string.settings_reset_transport_details,
            R.drawable.icon_restore,
            ACTION_RESET_ROUTE
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_transport_last,
            R.string.settings_transport_last_update,
            0,
            ACTION_TRANSPORT_UPDATE
        ),
        arrayOf(VIEW_LINE),
        arrayOf(VIEW_HEADER, R.string.settings_header_legal),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_terms,
            R.string.settings_terms_details,
            R.drawable.icon_copyright,
            ACTION_TERMS
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_privacy,
            R.string.settings_privacy_details,
            0,
            ACTION_PRIVACY
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_acknow,
            R.string.settings_acknow_details,
            R.drawable.icon_fav,
            ACTION_ACK
        ), arrayOf(
            VIEW_ITEM,
            R.string.settings_crash_report,
            R.string.settings_crash_report_details,
            R.drawable.icon_bug,
            ACTION_CRASH_REPORT
        ),
        arrayOf(VIEW_LINE),
        arrayOf(VIEW_HEADER, R.string.settings_header_about),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_about,
            R.string.settings_about_details,
            R.drawable.icon_star,
            ACTION_ABOUT_US
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_github,
            R.string.settings_github_details,
            R.drawable.icon_github,
            ACTION_GITHUB
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_feedback,
            R.string.settings_feedback_details,
            R.drawable.icon_feedback,
            ACTION_FEEDBACK
        ),
        arrayOf(
            VIEW_ITEM,
            R.string.settings_app_details,
            R.string.settings_app_version,
            R.mipmap.ic_launcher_round,
            ACTION_APP_DETAILS
        )
    )

    private fun convertFormat(input: String): String {
        val inputFormat = SimpleDateFormat(Constants.FORMAT_SERVER_TIMESTAMP, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(Constants.FORMAT_MODIFIED_WITH_TIME, Locale.ENGLISH)
        val tempCal = Calendar.getInstance()
        try {
            tempCal.timeInMillis = inputFormat.parse(input)?.time!!
        } catch (e: ParseException) {
            return "N/A"
        }
        return outputFormat.format(Date(tempCal.timeInMillis))
    }

    private fun changeCrashReporting() {
        context?.toast("Crash Reporting Preference Updated")
        repository.prefs().crashReportingEnabled(!repository.prefs().crashReportingEnabled())
        setUpItems()
    }

    private fun gotoGitHub() {
        val currentUrl = "https://github.com/NCBSinfo/NCBSinfo"
        val i2 = Intent(Intent.ACTION_VIEW)
        i2.data = Uri.parse(currentUrl)
        startActivity(i2)
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf("contact@secretbiology.com", "ncbs.mod@gmail.com")
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on NCBSinfo v" + BuildConfig.VERSION_CODE)
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    private fun resetTransport() {

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.are_you_sure))
            .setCancelable(false)
            .setMessage(getString(R.string.settings_reset_warning))
            .setPositiveButton(
                R.string.yes
            ) { d, _ ->
                callback?.showProgress()
                viewModel.resetRoutes(repository)
                d.dismiss()
            }.setNegativeButton(
                android.R.string.cancel
            ) { _, _ -> }.show()

    }

    override fun itemClicked(action: Int) {
        when (action) {
            ACTION_ABOUT_US -> {
                callback?.showSettingsInfo(ACTION_ABOUT_US)
            }
            ACTION_PRIVACY -> {
                callback?.showSettingsInfo(ACTION_PRIVACY)
            }
            ACTION_ACK -> {
                callback?.showSettingsInfo(ACTION_ACK)
            }
            ACTION_TERMS -> {
                callback?.showSettingsInfo(ACTION_TERMS)
            }
            ACTION_NOTICE -> {
                callback?.showSettingsInfo(ACTION_NOTICE)
            }
            ACTION_GITHUB -> {
                gotoGitHub()
            }
            ACTION_FEEDBACK -> {
                sendFeedback()
            }
            ACTION_RESET_ROUTE -> {
                resetTransport()
            }
            ACTION_DEFAULT_ROUTE -> {
                viewModel.getRouteList(repository)
            }
            ACTION_CRASH_REPORT -> {
                changeCrashReporting()
            }
        }
    }
}
