package com.ender.ribs.contacts.list.interactor

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.ender.ribs.contacts.list.ContactsList
import com.ender.ribs.contacts.list.feature.ContactsListFeature
import com.ender.ribs.contacts.list.feature.ContactsListFeature.State
import com.ender.ribs.contacts.list.feature.ContactsListFeature.Wish
import com.ender.ribs.contacts.list.model.Contact
import com.ender.ribs.contacts.list.view.ContactsListView
import com.ender.ribs.contacts.list.view.ContactsListView.Event
import com.ender.ribs.contacts.list.view.NewsListener
import com.ender.search.Search
import io.reactivex.functions.Consumer

private object StateToViewModelMapper : (State) -> ContactsListView.ViewModel {
    override fun invoke(state: State): ContactsListView.ViewModel =
        ContactsListView.ViewModel(
            state.isLoading,
            state.contacts.map {
                Contact(it.name, it.email, it.isFavourite, it.phoneNumber, it.avatarUrl)
            }
        )
}

private object ViewEventToWishMapper : (Event) -> Wish? {
    override fun invoke(event: Event): Wish? =
        when (event) {
            is Event.ButtonClicked -> Wish.LoadContacts
            is Event.ContactMarkedAsFavourite -> Wish.FavouriteContact(event.emailAddress)
            is Event.ContactUnMarkedAsFavourite -> Wish.UnFavouriteContact(event.emailAddress)
            else -> null
        }
}

private object ViewEventToOutputMapper : (Event) -> ContactsList.Output? {
    override fun invoke(event: Event): ContactsList.Output? =
        when (event) {
            is Event.ContactSelected -> ContactsList.Output.ContactSelected(event.emailAddress)
            else -> null
        }
}

private object InputToWish : (ContactsList.Input) -> Wish {
    override fun invoke(input: ContactsList.Input): Wish {
        return when (input) {
            is ContactsList.Input.Search -> Wish.SearchContact(input.keyword)
        }
    }
}

internal class ContactsListInteractor(
    buildParams: BuildParams<Nothing?>,
    private val feature: ContactsListFeature,
) : Interactor<ContactsList, ContactsListView>(buildParams) {

    private val contactsListListener = Consumer<Search.Output> { output ->
        when (output) {
            is Search.Output.KeywordInserted -> rib.input.accept(ContactsList.Input.Search(output.keyword))
        }
    }

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(rib.input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: ContactsListView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModelMapper)
            bind(feature.news to NewsListener(view.androidView, feature))
            bind(view to feature using ViewEventToWishMapper)
            bind(view to rib.output using ViewEventToOutputMapper)
        }
    }

    override fun onChildCreated(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Search -> {
                    bind(child.output to contactsListListener)
                }
            }
        }
    }
}