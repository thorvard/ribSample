package com.ender.ribs.contacts.container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.ender.ribs.contacts.container.view.ContainerAndroidView
import com.ender.ribs.contacts.container.view.ContainerView
import com.ender.ribs.storage.LocalStorage

interface Container : Rib {

    interface Dependency {
        val localStorage: LocalStorage
    }

    class Customisation(
        val viewFactory: ContainerView.Factory = ContainerAndroidView.Factory()
    ) : RibCustomisation

}