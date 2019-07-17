package com.rohitsuratekar.NCBSinfo.fragments


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.ContactDetailsAdapter
import com.rohitsuratekar.NCBSinfo.models.Contact
import kotlinx.android.synthetic.main.fragment_contact_details.*


class ContactDetailsFragment : BottomSheetDialogFragment(), ContactDetailsAdapter.OnCalled {

    private var currentContact: Contact? = null
    private var listener: ContactDetailsAdapter.OnCalled? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            currentContact = Contact().apply {
                name = ContactDetailsFragmentArgs.fromBundle(it).name
                primaryExtension = ContactDetailsFragmentArgs.fromBundle(it).primary
                otherExtensions = ContactDetailsFragmentArgs.fromBundle(it).extra
                details = ContactDetailsFragmentArgs.fromBundle(it).details

            }
        }

        setUpUI()
    }

    private fun setUpUI() {
        currentContact?.let { contact ->
            ct_sheet_name.text = contact.name
            ct_sheet_feedback.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/html"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@secretbiology.com", "ncbs.mod@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on " + contact.name + " in contact list")
                startActivity(Intent.createChooser(intent, "Send Email"))
                dismiss()
            }
            ct_sheet_recycler.adapter = ContactDetailsAdapter(contact, this)
            ct_sheet_recycler.layoutManager = LinearLayoutManager(context)

            ct_sheet_download.setOnClickListener { saveContact() }
        }
    }

    private fun getFormattedNumber(string: String?): String {
        string?.let {
            return when {
                it.trim().startsWith("6") and (it.trim().length == 4) -> "+91 80 2366 $it"
                it.trim().startsWith("5") and (it.trim().length == 4) -> "+91 80 2308 $it"
                else -> it
            }
        }
        return ""
    }

    private fun saveContact() {
        currentContact?.let {
            val intent = Intent(Intent.ACTION_INSERT)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE
            intent.putExtra(ContactsContract.Intents.Insert.NAME, it.name)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, getFormattedNumber(it.primaryExtension))

            val data = ArrayList<ContentValues>()
            for (a in it.extraExtensions()) {
                val row = ContentValues()
                row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, getFormattedNumber(a))
                data.add(row)
            }
            if (data.isNotEmpty()) {
                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
            }

            context?.startActivity(intent)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactDetailsAdapter.OnCalled) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ContactDetailsAdapter.OnCalled")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun calledNumber(number: String) {
        dismiss()
        listener?.calledNumber(number)
    }


}
