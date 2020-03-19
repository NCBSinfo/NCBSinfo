package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohitsuratekar.NCBSinfo.MainActivity
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.TransportRoutesAdapter
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.viewmodels.SharedViewModel
import com.rohitsuratekar.NCBSinfo.viewmodels.TransportRoutesViewModel
import kotlinx.android.synthetic.main.fragment_transport_routes.*

class TransportRoutesFragment : BottomSheetDialogFragment(), TransportRoutesAdapter.OnRouteClick {

    private var viewModel: TransportRoutesViewModel? = null
    private var sharedModel: SharedViewModel? = null
    private lateinit var repository: Repository
    private lateinit var adapter: TransportRoutesAdapter
    private var routeID: Int = 0
    private val routeList = mutableListOf<RouteData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProvider(this).get(TransportRoutesViewModel::class.java)
            sharedModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }
        repository = (activity as MainActivity).repository
        adapter = TransportRoutesAdapter(routeList, 0, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transport_routes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            routeID = TransportRoutesFragmentArgs.fromBundle(it).routeID
        }
        subscribe()
        tp_sheet_recycler.adapter = adapter
        tp_sheet_recycler.layoutManager = GridLayoutManager(context, 2)
        viewModel?.fetchRoutes(repository)
    }

    private fun subscribe() {
        viewModel?.routeList?.observe(viewLifecycleOwner, Observer {
            routeList.clear()
            routeList.addAll(it)
            adapter.setRouteID(routeID)
            adapter.notifyDataSetChanged()
        })
        viewModel?.currentRoute?.observe(viewLifecycleOwner, Observer {
            sharedModel?.changeCurrentRoute(it)
        })
    }

    override fun clicked(routeData: RouteData) {
        viewModel?.changeCurrentRoute(repository, routeData)
        dismiss()
    }


}
