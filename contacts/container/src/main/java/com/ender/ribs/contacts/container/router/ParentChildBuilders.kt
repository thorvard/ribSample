package com.ender.ribs.contacts.container.router

import com.ender.ribs.contacts.container.Container
import com.ender.ribs.contacts.detail.ContactDetail
import com.ender.ribs.contacts.detail.builder.ContactDetailBuilder
import com.ender.ribs.contacts.list.ContactsList
import com.ender.ribs.contacts.list.builder.ContactsListBuilder

class ParentChildBuilders(
    dependency: Container.Dependency
) {

    private val subtreeDependency = SubtreeDependency(dependency)

    val contactList: ContactsListBuilder = ContactsListBuilder(subtreeDependency)
    val contactDetail: ContactDetailBuilder = ContactDetailBuilder(subtreeDependency)

    class SubtreeDependency(
        dependency: Container.Dependency
    ) : Container.Dependency by dependency, ContactsList.Dependency, ContactDetail.Dependency

}

