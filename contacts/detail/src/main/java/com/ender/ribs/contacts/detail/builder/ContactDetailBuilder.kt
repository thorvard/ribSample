package com.ender.ribs.contacts.detail.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.ender.ribs.contacts.detail.ContactDetail
import com.ender.ribs.contacts.detail.ContactDetailNode
import com.ender.ribs.contacts.detail.interactor.ContactsDetailInteractor
import com.ender.ribs.contacts.detail.data.ContactsRepositoryImpl
import com.ender.ribs.contacts.detail.feature.ContactDetailFeature

class ContactDetailBuilder(
    private val dependency: ContactDetail.Dependency
) : Builder<ContactDetailBuilder.Params, ContactDetail>() {

    data class Params(
        val emailAddress: String
    )

    override fun build(buildParams: BuildParams<Params>): ContactDetail {
        val repository = ContactsRepositoryImpl(dependency.localStorage)
        val customisation = buildParams.getOrDefault(ContactDetail.Customisation())
        val feature = ContactDetailFeature(repository, buildParams.payload.emailAddress)
        val interactor = ContactsDetailInteractor(buildParams = buildParams, feature = feature)

        return ContactDetailNode(buildParams, customisation.viewFactory(null), listOf(interactor))
    }
}
