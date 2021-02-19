package com.ender.ribs.contacts.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ender.ribs.contacts.list.model.Contact
import com.ender.ribs.contacts.list.R
import com.ender.ribs.contacts.list.databinding.RowContactBinding
import com.squareup.picasso.Picasso

internal class ContactsAdapter(
    private var avatarClickListener: ((emailAddress: String) -> Unit),
    private var favouriteClickListener: ((emailAddress: String) -> Unit),
    private var unFavouriteClickListener: ((emailAddress: String) -> Unit)
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    private val contacts: AsyncListDiffer<Contact> =
        AsyncListDiffer(this, ContactsDiffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = RowContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val contactViewHolder = ContactViewHolder(binding)
        setClickListenerPerViewHolder(contactViewHolder)
        return contactViewHolder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts.currentList[position])
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(FAVOURITE))
                holder.updateFavouriteStatus(bundle.getBoolean(FAVOURITE))
        }
    }

    override fun getItemCount(): Int = contacts.currentList.size

    fun addContacts(contacts: List<Contact>) {
        this.contacts.submitList(contacts)
    }

    private fun setClickListenerPerViewHolder(contactViewHolder: ContactViewHolder) {
        setAvatarClickListener(contactViewHolder)
        setFavouriteClickListener(contactViewHolder)
    }

    private fun setAvatarClickListener(contactViewHolder: ContactViewHolder) {
        contactViewHolder.avatar.setOnClickListener {
            val position = contactViewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val emailAddress = contacts.currentList[position].email
                avatarClickListener(emailAddress)
            }
        }
    }

    private fun setFavouriteClickListener(contactViewHolder: ContactViewHolder) {
        contactViewHolder.favouriteIcon.setOnClickListener {
            val position = contactViewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val emailAddress = contacts.currentList[position].email
                if (contacts.currentList[position].isFavourite)
                    unFavouriteClickListener.invoke(emailAddress)
                else
                    favouriteClickListener.invoke(emailAddress)
            }
        }
    }

    class ContactViewHolder(private val binding: RowContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val favouriteIcon: View = binding.favouriteIcon
        val avatar: View = binding.avatar

        fun bind(contact: Contact) {
            with(binding) {
                fullName.text = contact.name
                emailAddress.text = contact.email
                phoneNumber.text = contact.phoneNumber
                updateFavouriteStatus(contact.isFavourite)
                Picasso.get()
                    .load(contact.avatarUrl)
                    .into(binding.avatar)
            }
        }

        fun updateFavouriteStatus(isFavourite: Boolean) {
            if (isFavourite)
                binding.favouriteIcon.setImageResource(R.drawable.ic_baseline_favorite)
            else
                binding.favouriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border)
        }
    }

    companion object {
        const val FAVOURITE = "is_favourite"
    }
}