package com.ender.ribs.contacts.detail

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.ender.ribs.contacts.detail.view.ContactDetailView

class ContactDetailNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContactDetailView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContactDetailView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContactDetail