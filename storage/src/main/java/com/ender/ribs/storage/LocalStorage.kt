package com.ender.ribs.storage

import com.ender.ribs.storage.model.ContactEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.reflect.Type

interface LocalStorage {
    fun getContacts(): Observable<List<ContactEntity>>
    fun loadContact(emailAddress: String): Maybe<ContactEntity>
    fun toggleContactFavouriteStatus(emailAddress: String): Completable
}

internal class InMemoryLocalStorage : LocalStorage {
    private val storedContacts: MutableMap<String, ContactEntity> = mutableMapOf()
    private val subject: BehaviorSubject<List<ContactEntity>> = BehaviorSubject.create()

    init {
        val classLoader: ClassLoader = this.javaClass.classLoader
        val inputStream: InputStream = classLoader.getResourceAsStream("data.json")
        val reader: Reader = InputStreamReader(inputStream, "UTF-8")

        val founderListType: Type = object : TypeToken<ArrayList<ContactEntity>>() {}.type
        val result: List<ContactEntity> = Gson().fromJson(reader, founderListType)
        result.forEach { storedContacts[it.email] = it }

        subject.onNext(storedContacts.values.toList())
    }

    override fun getContacts(): Observable<List<ContactEntity>> = subject

    override fun loadContact(emailAddress: String): Maybe<ContactEntity> =
        Maybe.fromCallable { storedContacts[emailAddress] }

    override fun toggleContactFavouriteStatus(emailAddress: String): Completable =
        Completable.fromAction {
            storedContacts[emailAddress]?.run {
                storedContacts[emailAddress] = copy(isFavourite = !this.isFavourite)
                subject.onNext(storedContacts.values.toList())
            } ?: throw Exception()
        }
}

