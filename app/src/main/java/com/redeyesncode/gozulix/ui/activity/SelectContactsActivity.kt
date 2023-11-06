package com.redeyesncode.gozulix.ui.activity

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.gozulix.data.ContactItem
import com.redeyesncode.gozulix.databinding.ActivitySelectContactsBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.adapters.MyContactAdapter
import com.redeyesncode.redbet.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SelectContactsActivity : BaseActivity() {
    private lateinit var contactList: List<ContactItem>

    lateinit var binding:ActivitySelectContactsBinding
    lateinit var contactAdapter:MyContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectContactsBinding.inflate(layoutInflater)
        setupContactRecycler()
        setupSearchView()
        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.btnOk.setOnClickListener {
            val selectedContacts = contactAdapter.getSelectedContacts()
            finish()
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submit if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter((newText.orEmpty()))
                return true
            }
        })

    }
    private fun collectSelectedContacts(): List<ContactItem> {
        val selectedContacts = mutableListOf<ContactItem>()
        for (contact in contactList) {
            if (contact.selected) {
                showToast("SELECTED")
                selectedContacts.add(contact)
            }
        }
        return selectedContacts
    }

    fun filter(query: String) {
        val data = fetchContacts(this@SelectContactsActivity)
        val lowercaseQuery = query.toLowerCase(Locale.getDefault())
        contactList = if (query.isEmpty()) {
            data
        } else {
            data.filter { it.contactName.toLowerCase(Locale.getDefault()).contains(lowercaseQuery) }
        }
        contactAdapter = MyContactAdapter(this@SelectContactsActivity,contactList)
        binding.recvContacts.adapter = contactAdapter
        binding.recvContacts.layoutManager = LinearLayoutManager(this@SelectContactsActivity,LinearLayoutManager.VERTICAL,false)

    }
    private fun setupContactRecycler() {
        contactList = fetchContacts(this@SelectContactsActivity)
        binding.materialToolbar.title = "Choose Contacts (${contactList.size})"
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnOk.setOnClickListener {

            // check the list and update the room db logic.
            val selectedContacts = contactAdapter.getSelectedContacts()

            lifecycleScope.launch {
                insertContact(selectedContacts)
            }


        }
        contactAdapter = MyContactAdapter(this@SelectContactsActivity,contactList)
        binding.recvContacts.adapter = contactAdapter
        binding.recvContacts.layoutManager = LinearLayoutManager(this@SelectContactsActivity,LinearLayoutManager.VERTICAL,false)


    }

    suspend fun insertContact(selectedContacts: List<ContactItem>) {
        val contactDatabase = ContactDatabase.getDatabase(applicationContext)
        val contactDao = contactDatabase.contactDao()

        if(selectedContacts.isNotEmpty()){
            for(contact in selectedContacts){
                val newContact = ContactEntity(
                    contactName = contact.contactName,
                    contactNumber = contact.contactNumber,
                    status = "Pending",
                    note = "No Note Present"
                )
                withContext(Dispatchers.IO) {
                    contactDao.insert(newContact)

                }
            }
        }


    }

    fun fetchContacts(context: Context): List<ContactItem> {
        val contactList = mutableListOf<ContactItem>()
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // Contact name
            ContactsContract.CommonDataKinds.Phone.NUMBER       // Phone number
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val contactName = it.getString(nameIndex) ?: "Unknown"
                val contactNumber = it.getString(numberIndex) ?: ""

                val contactItem = ContactItem(contactName, contactNumber)
                contactList.add(contactItem)
            }
        }

        return contactList
    }
}