package com.ender.ribs.contacts.list

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.ender.ribs.contacts.list.view.ContactsListAndroidView
import com.ender.ribs.contacts.list.view.ContactsListView
import com.ender.ribs.storage.LocalStorage

interface ContactsList : Rib, Connectable<Nothing, ContactsList.Output> {

    interface Dependency {
        val localStorage: LocalStorage
    }

    sealed class Output {
        data class ContactSelected(val contactEmail: String) : Output()
    }

    class Customisation(
        val viewFactory: ContactsListView.Factory = ContactsListAndroidView.Factory()
    ) : RibCustomisation

}