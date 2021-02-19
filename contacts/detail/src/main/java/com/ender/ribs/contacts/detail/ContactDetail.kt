package com.ender.ribs.contacts.detail

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.ender.ribs.contacts.detail.view.ContactDetailAndroidView
import com.ender.ribs.contacts.detail.view.ContactDetailView
import com.ender.ribs.storage.LocalStorage

interface ContactDetail : Rib {

    interface Dependency {
        val localStorage: LocalStorage
    }

    class Customisation(
        val viewFactory: ContactDetailView.Factory = ContactDetailAndroidView.Factory()
    ) : RibCustomisation

}