package com.ender.ribs.contacts.list

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.ender.ribs.contacts.list.view.ContactsListView

internal class ContactsListNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContactsListView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Nothing, ContactsList.Output> = NodeConnector()
) : Node<ContactsListView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContactsList, Connectable<Nothing, ContactsList.Output> by connector