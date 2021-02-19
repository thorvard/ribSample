package com.ender.ribs.contacts.detail.interactor

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.ender.ribs.contacts.detail.ContactDetail
import com.ender.ribs.contacts.detail.builder.ContactDetailBuilder
import com.ender.ribs.contacts.detail.feature.ContactDetailFeature
import com.ender.ribs.contacts.detail.view.ContactDetailView

private object StateToViewModelMapper :
        (ContactDetailFeature.State) -> ContactDetailView.ViewModel {
    override fun invoke(p1: ContactDetailFeature.State): ContactDetailView.ViewModel =
        ContactDetailView.ViewModel(p1.isLoading, p1.contact, null)
}

class ContactsDetailInteractor(
    buildParams: BuildParams<ContactDetailBuilder.Params>,
    private val feature: ContactDetailFeature
) : Interactor<ContactDetail, ContactDetailView>(buildParams) {

    override fun onViewCreated(view: ContactDetailView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModelMapper)
        }
    }
}