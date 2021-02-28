package com.ender.ribs.contacts.list.data

import com.ender.ribs.contacts.list.data.specification.GenericFieldSpecification
import com.ender.ribs.contacts.list.model.Contact
import com.ender.ribs.storage.LocalStorage
import com.ender.ribs.storage.model.ContactEntity
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

internal interface ContactsRepository {
    fun getContacts(): Observable<List<Contact>>
    fun searchContacts(keyword: String): Completable
    fun clearSearch(): Completable
    fun toggleContactFavouriteStatus(emailAddress: String): Completable
}

internal class ContactsRepositoryImpl(
    private val localStorage: LocalStorage
) : ContactsRepository {
    override fun getContacts(): Observable<List<Contact>> =
        localStorage.getContacts()
            .delay(1, TimeUnit.SECONDS)
            .map { contacts -> contacts.map { it.toDomain() } }

    override fun searchContacts(keyword: String): Completable =
        localStorage.setFilterSpecification(GenericFieldSpecification(keyword))

    override fun clearSearch(): Completable =
        localStorage.clearFilter()

    override fun toggleContactFavouriteStatus(emailAddress: String): Completable =
        localStorage.toggleContactFavouriteStatus(emailAddress)

    private fun ContactEntity.toDomain() =
        Contact("$first $last", email, isFavourite, phone, thumbnail)
}