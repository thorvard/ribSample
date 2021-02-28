package com.ender.ribs.contacts.list

import android.content.Context
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.ender.ribs.contacts.list.view.ContactsListAndroidView
import com.ender.ribs.contacts.list.view.ContactsListView
import com.ender.ribs.storage.LocalStorage

interface ContactsList : Rib, Connectable<ContactsList.Input, ContactsList.Output> {

    interface Dependency {
        val localStorage: LocalStorage
        val context: Context
    }

    sealed class Input {
        data class Search(val keyword: String) : Input()
    }

    sealed class Output {
        data class ContactSelected(val contactEmail: String) : Output()
    }

    class Customisation(
        val viewFactory: ContactsListView.Factory = ContactsListAndroidView.Factory()
    ) : RibCustomisation

}