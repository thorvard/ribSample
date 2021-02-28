package com.ender.ribs.contacts.list.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.ender.ribs.contacts.list.data.ContactsRepository
import com.ender.ribs.contacts.list.feature.ContactsListFeature.*
import com.ender.ribs.contacts.list.model.Contact
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

internal class ContactsListFeature(
    contactsRepository: ContactsRepository
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(contactsRepository),
    reducer = ReducerImpl(),
    wishToAction = { Action.Launch(it) },
    newsPublisher = NewsPublisherImpl(),
    bootstrapper = BootstrapperImpl(contactsRepository)
) {

    data class State(
        val isLoading: Boolean = false,
        val contacts: List<Contact> = arrayListOf()
    )

    sealed class Wish {
        object LoadContacts : Wish()
        data class SearchContact(val keyword: String) : Wish()
        data class FavouriteContact(val emailAddress: String) : Wish()
        data class UnFavouriteContact(val emailAddress: String) : Wish()
    }

    sealed class Action {
        data class Launch(val wish: Wish) : Action()
        data class ContactsLoaded(val contacts: List<Contact>) : Action()
    }

    sealed class Effect {
        object Loading : Effect()
        data class ContactsLoaded(val contacts: List<Contact>) : Effect()
        data class ErrorOnContactLoad(val throwable: Throwable) : Effect()
        data class GenericError(val throwable: Throwable) : Effect()
    }

    sealed class News {
        object ContactLoadError : News()
        object GenericError : News()
    }

    private class ActorImpl(private val contactsRepository: ContactsRepository) :
        Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<out Effect> =
            when (action) {
                is Action.Launch -> when (action.wish) {
                    is Wish.LoadContacts -> loadContacts()
                    is Wish.SearchContact -> {
                        if (action.wish.keyword.isEmpty()) clearSearch()
                        else searchContacts(action.wish.keyword)
                    }
                    is Wish.FavouriteContact -> toggleContactFavouriteStatus(action.wish.emailAddress)
                    is Wish.UnFavouriteContact -> toggleContactFavouriteStatus(action.wish.emailAddress)
                }
                is Action.ContactsLoaded -> Observable.just(Effect.ContactsLoaded(action.contacts))
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        private fun loadContacts(): Observable<Effect> =
            contactsRepository.getContacts()
                .map { Effect.ContactsLoaded(it) }
                .cast(Effect::class.java)
                .startWith(Effect.Loading)
                .onErrorReturn { Effect.ErrorOnContactLoad(it) }

        private fun searchContacts(keyword: String): Observable<Effect> =
            contactsRepository.searchContacts(keyword)
                .toObservable<Effect>()
                .debounce(500, TimeUnit.MILLISECONDS)
                .startWith(Effect.Loading)

        private fun clearSearch(): Observable<Effect> =
            contactsRepository.clearSearch()
                .toObservable<Effect>()
                .startWith(Effect.Loading)

        private fun toggleContactFavouriteStatus(emailAddress: String): Observable<Effect> =
            contactsRepository.toggleContactFavouriteStatus(emailAddress)
                .toObservable<Effect>()
                .startWith(Effect.Loading)
                .onErrorReturn { Effect.GenericError(it) }
    }

    private class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.Loading -> state.copy(isLoading = true)
                is Effect.ContactsLoaded -> state.copy(
                    isLoading = false,
                    contacts = effect.contacts
                )
                is Effect.ErrorOnContactLoad,
                is Effect.GenericError -> state.copy(isLoading = false)
            }
    }

    private class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? =
            when (effect) {
                is Effect.ErrorOnContactLoad -> News.ContactLoadError
                is Effect.GenericError -> News.GenericError
                else -> null
            }
    }

    class BootstrapperImpl(private val repository: ContactsRepository) : () -> Observable<Action> {
        override fun invoke(): Observable<Action> {
            return repository
                .getContacts()
                .map(Action::ContactsLoaded)
        }
    }
}