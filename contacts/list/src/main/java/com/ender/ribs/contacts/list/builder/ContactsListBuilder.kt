package com.ender.ribs.contacts.list.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.ender.ribs.contacts.list.ContactsList
import com.ender.ribs.contacts.list.ContactsListNode
import com.ender.ribs.contacts.list.data.ContactsRepositoryImpl
import com.ender.ribs.contacts.list.feature.ContactsListFeature
import com.ender.ribs.contacts.list.interactor.ContactsListInteractor

class ContactsListBuilder(
    private val dependency: ContactsList.Dependency
) : SimpleBuilder<ContactsList>() {
    override fun build(buildParams: BuildParams<Nothing?>): ContactsList {
        val repository = ContactsRepositoryImpl(dependency.localStorage)
        val customisation = buildParams.getOrDefault(ContactsList.Customisation())
        val feature = ContactsListFeature(repository)

        val interactor = ContactsListInteractor(
            buildParams = buildParams,
            feature = feature
        )

        return ContactsListNode(buildParams, customisation.viewFactory(null), listOf(interactor))
    }
}