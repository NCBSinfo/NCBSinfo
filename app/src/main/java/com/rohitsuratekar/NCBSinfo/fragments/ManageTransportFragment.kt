package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        viewModel.getRouteList(repository)
        adapter = ManageTransportAdapter(routeList, this)
        mt_recycler.adapter = adapter
        mt_recycler.layoutManager = LinearLayoutManager(context)
        subscribe()
    }

    private fun subscribe() {
        viewModel.routeList.observe(this, Observer {
            routeList.clear()
            routeList.addAll(it)
            adapter.notifyDataSetChanged()
        })
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun report(route: Route) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
