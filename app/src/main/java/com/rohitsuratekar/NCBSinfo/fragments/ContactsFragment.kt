package com.rohitsuratekar.NCBSinfo.fragments


import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.ContactAdapter
import com.rohitsuratekar.NCBSinfo.models.Contact
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.getAllContacts
import kotlinx.android.synthetic.main.fragment_contacts.*
import java.util.*

class ContactsFragment : MyFragment(), ContactAdapter.OnContactSelect {

    private var searchView: SearchView? = null
    private var contactList = mutableListOf<Contact>()
    private var originalList = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter
    private var sortOder = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactList.addAll(getAllContacts())
        originalList.addAll(getAllContacts())
        adapter = ContactAdapter(contactList, this)
        contact_recycler.adapter = adapter
        contact_recycler.layoutManager = LinearLayoutManager(context)
        setUpToolBar()
    }

    private fun setUpToolBar() {
        contact_toolbar.setTitle(R.string.contacts)
        contact_toolbar.inflateMenu(R.menu.contact_menu)
        for (menuItems in contact_toolbar.menu.iterator()) {
            menuItems.icon.setColorFilter(
                ContextCompat.getColor(context!!, R.color.colorLight),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        val searchItem = contact_toolbar.menu.findItem(R.id.action_search)
        searchItem?.let {
            searchView = it.actionView as SearchView
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView!!.clearFocus()
                searchItem?.collapseActionView()
                searchView!!.isIconified = true
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contactList.clear()
                if (!newText.isNullOrEmpty()) {
                    for (c in originalList) {
                        c.searchArea.clear()
                        c.name?.let {
                            if (it.toLowerCase().contains(newText.toLowerCase())) {
                                c.searchArea.add(Contact.AREA.NAME)
                            }
                        }
                        c.details?.let {
                            if (it.toLowerCase().contains(newText.toLowerCase())) {
                                c.searchArea.add(Contact.AREA.DETAILS)
                            }
                        }
                        c.primaryExtension?.let {
                            if (it.toLowerCase().contains(newText.toLowerCase())) {
                                c.searchArea.add(Contact.AREA.EXTENSION)
                            }
                        }

                        if (c.searchArea.size > 0) {
                            c.searchString = newText
                            contactList.add(c)
                        }
                    }
                } else {
                    for (c in originalList) {
                        c.searchArea.clear()
                        c.searchString = ""
                    }
                    contactList.addAll(originalList)
                }
                adapter.notifyDataSetChanged()
                return true
            }

        })

        contact_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_sort -> {
                    when (sortOder) {
                        0 -> {
                            it.setIcon(R.drawable.icon_sort_alphabetical)
                            it.icon.setColorFilter(
                                ContextCompat.getColor(context!!, R.color.colorLight),
                                PorterDuff.Mode.SRC_ATOP
                            )
                            originalList.sortWith(Comparator { o1, o2 -> o1.name!!.compareTo(o2.name!!) })
                            sortOder = 1
                        }
                        1 -> {
                            originalList.reverse()
                            sortOder = 2
                        }
                        else -> {
                            it.setIcon(R.drawable.icon_sort)
                            it.icon.setColorFilter(
                                ContextCompat.getColor(context!!, R.color.colorLight),
                                PorterDuff.Mode.SRC_ATOP
                            )
                            originalList.clear()
                            originalList.addAll(getAllContacts())
                            sortOder = 0
                        }
                    }
                    contactList.clear()
                    contactList.addAll(originalList)
                    adapter.notifyDataSetChanged()

                }
            }
            return@setOnMenuItemClickListener true
        }

    }

    override fun contactSelected(contact: Contact) {
        callback?.showContact(contact)
    }


}
