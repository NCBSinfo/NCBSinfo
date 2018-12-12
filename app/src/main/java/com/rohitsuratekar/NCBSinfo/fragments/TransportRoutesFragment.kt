package com.rohitsuratekar.NCBSinfo.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohitsuratekar.NCBSinfo.MainActivity
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.TransportRoutesAdapter
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.di.Repository
import com.rohitsuratekar.NCBSinfo.viewmodels.TransportRoutesViewModel
import kotlinx.android.synthetic.main.fragment_transport_routes.*

class TransportRoutesFragment : BottomSheetDialogFragment(), TransportRoutesAdapter.OnRouteClick {

    private var viewModel: TransportRoutesViewModel? = null
    private lateinit var repository: Repository
    private lateinit var adapter: TransportRoutesAdapter
    private var listener: OnRouteChanged? = null
    private var routeID: Int = 0
    private val routeList = mutableListOf<RouteData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel = activity?.run {
                ViewModelProviders.of(this).get(TransportRoutesViewModel::class.java)
            }
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
        viewModel?.routeList?.observe(this, Observer {
            routeList.clear()
            routeList.addAll(it)
            adapter.setRouteID(routeID)
            adapter.notifyDataSetChanged()
        })
    }

    override fun clicked(routeData: RouteData) {
        //listener?.newRoute(routeData)
        viewModel?.changeCurrentRoute(routeData)
        dismiss()

    }

    interface OnRouteChanged {
        fun newRoute(routeData: RouteData)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRouteChanged) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnRouteChanged")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
