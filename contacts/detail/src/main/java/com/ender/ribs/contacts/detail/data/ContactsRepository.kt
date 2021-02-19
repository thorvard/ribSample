package com.ender.ribs.contacts.detail.data

import com.ender.ribs.contacts.detail.model.Contact
import com.ender.ribs.storage.LocalStorage
import com.ender.ribs.storage.model.ContactEntity
import io.reactivex.Maybe

interface ContactsRepository {

    fun loadContact(emailAddress: String): Maybe<Contact>

}

class ContactsRepositoryImpl(private val localStorage: LocalStorage) : ContactsRepository {
    override fun loadContact(emailAddress: String): Maybe<Contact> =
        localStorage.loadContact(emailAddress)
            .map { it.toDomain() }

    private fun ContactEntity.toDomain() =
        Contact("$first $last", email, isFavourite, phone, imageLarge)
}