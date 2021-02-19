package com.ender.ribs.contacts.detail.view

import com.ender.ribs.contacts.detail.model.Contact
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.ender.ribs.contacts.detail.R
import com.ender.ribs.contacts.detail.databinding.ContactDetailViewBinding
import com.squareup.picasso.Picasso
import io.reactivex.functions.Consumer

interface ContactDetailView : RibView,
    Consumer<ContactDetailView.ViewModel> {

    data class ViewModel(
        val loading: Boolean,
        val contact: Contact?,
        val error: String?
    )

    interface Factory : ViewFactory<Nothing?, ContactDetailView>

}

class ContactDetailAndroidView(
    override val androidView: ViewGroup
) : AndroidRibView(), ContactDetailView,
    Consumer<ContactDetailView.ViewModel> {

    private var binding: ContactDetailViewBinding = ContactDetailViewBinding.bind(androidView)

    override fun accept(viewModel: ContactDetailView.ViewModel) {
        viewModel.contact?.run {
            binding.fullName.text = name
            binding.emailAddress.text = emailAddress
            Picasso.get()
                .load(avatarUrl)
                .into(binding.avatar)
        }
    }

    class Factory(
        @LayoutRes private val layoutResourceId: Int = R.layout.contact_detail_view
    ) : ContactDetailView.Factory {
        override fun invoke(p1: Nothing?): (RibView) -> ContactDetailView = {
            ContactDetailAndroidView(
                LayoutInflater.from(it.context)
                    .inflate(layoutResourceId, it.androidView, false) as ViewGroup
            )
        }
    }
}
