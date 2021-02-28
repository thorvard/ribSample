package com.ender.ribs.contacts.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.ender.ribs.contacts.list.R
import com.ender.ribs.contacts.list.databinding.ContactListViewBinding
import com.ender.ribs.contacts.list.model.Contact
import com.ender.ribs.contacts.list.view.ContactsListView.Event
import com.ender.ribs.contacts.list.view.ContactsListView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ContactsListView : RibView, ObservableSource<Event>, Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
        data class ContactSelected(val emailAddress: String) : Event()
        data class ContactMarkedAsFavourite(val emailAddress: String) : Event()
        data class ContactUnMarkedAsFavourite(val emailAddress: String) : Event()
    }

    data class ViewModel(
        val loading: Boolean,
        val contacts: List<Contact>
    )

    interface Factory : ViewFactory<Nothing?, ContactsListView>

}

internal class ContactsListAndroidView(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(), ContactsListView, ObservableSource<Event> by events, Consumer<ViewModel> {

    private var binding: ContactListViewBinding = ContactListViewBinding.bind(androidView)
    private val contactsAdapter =
        ContactsAdapter(::onAvatarClick, ::onFavouriteClick, ::onUnFavouriteClick)

    init {
        setUpContactsList()
    }

    override fun getParentViewForChild(subtreeOf: Node<*>): ViewGroup =
        binding.searchContainer

    override fun accept(viewModel: ViewModel) {
        binding.progressIndicator.visibility = if (viewModel.loading) View.VISIBLE else View.GONE
        contactsAdapter.addContacts(viewModel.contacts)
    }

    private fun setUpContactsList() {
        binding.contacts.apply {
            setHasFixedSize(true)
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun onAvatarClick(emailAddress: String) {
        events.accept(Event.ContactSelected(emailAddress))
    }

    private fun onFavouriteClick(emailAddress: String) {
        events.accept(Event.ContactMarkedAsFavourite(emailAddress))
    }

    private fun onUnFavouriteClick(emailAddress: String) {
        events.accept(Event.ContactUnMarkedAsFavourite(emailAddress))
    }

    class Factory(
        @LayoutRes private val layoutResourceId: Int = R.layout.contact_list_view
    ) : ContactsListView.Factory {
        override fun invoke(p1: Nothing?): (RibView) -> ContactsListView = {
            ContactsListAndroidView(
                LayoutInflater.from(it.context)
                    .inflate(layoutResourceId, it.androidView, false) as ViewGroup
            )
        }
    }
}
