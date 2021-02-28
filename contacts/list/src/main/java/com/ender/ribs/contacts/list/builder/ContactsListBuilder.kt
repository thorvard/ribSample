package com.ender.ribs.contacts.list.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.ender.ribs.contacts.list.ContactsList
import com.ender.ribs.contacts.list.ContactsListNode
import com.ender.ribs.contacts.list.data.ContactsRepositoryImpl
import com.ender.ribs.contacts.list.feature.ContactsListFeature
import com.ender.ribs.contacts.list.interactor.ContactsListInteractor
import com.ender.ribs.contacts.list.router.ContainerRouter
import com.ender.ribs.contacts.list.router.ParentChildBuilders

class ContactsListBuilder(
    private val dependency: ContactsList.Dependency
) : SimpleBuilder<ContactsList>() {

    private val builders by lazy { ParentChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): ContactsList {
        val repository = ContactsRepositoryImpl(dependency.localStorage)
        val customisation = buildParams.getOrDefault(ContactsList.Customisation())
        val feature = ContactsListFeature(repository)

        val router = ContainerRouter(
            buildParams = buildParams,
            childBuilders = builders
        )

        val interactor = ContactsListInteractor(
            buildParams = buildParams,
            feature = feature
        )

        return ContactsListNode(
            buildParams,
            customisation.viewFactory(null),
            listOf(interactor, router)
        )
    }
}