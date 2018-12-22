package com.rohitsuratekar.NCBSinfo.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.ManageTransportAdapter
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.viewmodels.ManageTransportViewModel
import kotlinx.android.synthetic.main.fragment_manage_transport.*

class ManageTransportFragment : MyFragment(), ManageTransportAdapter.OnOptionClicked {

    private lateinit var viewModel: ManageTransportViewModel
    private val routeList = mutableListOf<Route>()
    private lateinit var adapter: ManageTransportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_transport, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManageTransportViewModel::class.java)
        callback?.showProgress()
        viewModel.getRouteList(repository)
        adapter = ManageTransportAdapter(routeList, this)
        mt_recycler.adapter = adapter
        mt_recycler.layoutManager = LinearLayoutManager(context)
        mt_reset.setOnClickListener { resetRoutes() }
        subscribe()
    }

    private fun subscribe() {
        viewModel.routeList.observe(this, Observer {
            callback?.hideProgress()
            routeList.clear()
            routeList.addAll(it)
            adapter.notifyDataSetChanged()
        })
        viewModel.routeDeleted.observe(this, Observer {
            callback?.hideProgress()
            routeList.clear()
            routeList.addAll(it)
            adapter.notifyDataSetChanged()
            sharedModel.changeCurrentRoute(it[0])
        })
    }

    private fun resetRoutes() {
        context?.let {
            AlertDialog.Builder(it).setTitle(R.string.mt_reset_route_title)
                .setMessage(R.string.mt_reset_route)
                .setPositiveButton(
                    R.string.yes
                ) { d, _ ->
                    callback?.showProgress()
                    viewModel.resetRoutes(repository)
                    d.dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }.show()
        }
    }

    override fun expand(route: Route) {
        for (r in routeList) {
            if (r == route) {
                r.isExpanded = !r.isExpanded
            } else {
                r.isExpanded = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun edit(route: Route) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(route: Route) {
        var message = getString(
            R.string.mt_delete_confirm,
            route.routeData.origin?.toUpperCase(), route.routeData.destination?.toUpperCase(), route.routeData.type
        )
        if (routeList.size == 1) {
            message = getString(R.string.mt_single_route_error)
        }
        context?.let {
            AlertDialog.Builder(it).setTitle(R.string.are_you_sure)
                .setMessage(message)
                .setPositiveButton(
                    R.string.delete
                ) { d, _ ->
                    callback?.showProgress()
                    viewModel.deleteRoute(repository, route)
                    d.dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }.show()
        }

    }

    override fun report(route: Route) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@secretbiology.com", "ncbs.mod@gmail.com"))
        intent.putExtra(
            Intent.EXTRA_SUBJECT, getString(
                R.string.mt_route_feedback,
                route.routeData.origin?.toUpperCase(), route.routeData.destination?.toUpperCase(), route.routeData.type
            )
        )
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

}
