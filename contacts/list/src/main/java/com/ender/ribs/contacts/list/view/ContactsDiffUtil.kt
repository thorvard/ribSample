package com.ender.ribs.contacts.list.view

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.ender.ribs.contacts.list.model.Contact

internal object ContactsDiffUtil : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Contact, newItem: Contact): Any? {
        val bundle = Bundle()
        if (oldItem.isFavourite != newItem.isFavourite)
            bundle.putBoolean(ContactsAdapter.FAVOURITE, newItem.isFavourite)

        return if (!bundle.isEmpty)
            bundle
        else
            super.getChangePayload(oldItem, newItem)
    }
}