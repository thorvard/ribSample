package com.ender.ribs.contacts.detail.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.ender.ribs.contacts.detail.data.ContactsRepository
import com.ender.ribs.contacts.detail.feature.ContactDetailFeature.*
import com.ender.ribs.contacts.detail.model.Contact
import io.reactivex.Observable

class ContactDetailFeature(
    contactsRepository: ContactsRepository,
    contactEmail: String
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(contactsRepository),
    reducer = ReducerImpl(),
    wishToAction = { Action.Wish(it) },
    newsPublisher = NewsPublisherImpl(),
    bootstrapper = BootstrapperImpl(contactEmail)
) {

    data class State(
        val isLoading: Boolean = false,
        val contact: Contact? = null
    )

    sealed class Wish {
        data class LoadContact(val emailAddress: String) : Wish()
    }

    sealed class Action {
        data class Wish(val wish: ContactDetailFeature.Wish) : Action()
    }

    sealed class Effect {
        object Loading : Effect()
        data class ContactLoaded(val contact: Contact) : Effect()
        data class ErrorOnContactLoad(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ContactLoadError(val throwable: Throwable) : News()
    }

    class ActorImpl(private val contactsRepository: ContactsRepository) :
        Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<out Effect> =
            when (action) {
                is Action.Wish -> when (action.wish) {
                    is Wish.LoadContact -> contactsRepository.loadContact(action.wish.emailAddress)
                        .map { Effect.ContactLoaded(it) }
                        .cast(Effect::class.java)
                        .toObservable()
                        .startWith(Effect.Loading)
                        .onErrorReturn { Effect.ErrorOnContactLoad(it) }
                }
            }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.Loading -> state.copy(isLoading = true)
                is Effect.ContactLoaded -> state.copy(isLoading = false, contact = effect.contact)
                is Effect.ErrorOnContactLoad -> state.copy(isLoading = false)
            }
    }

    class BootstrapperImpl(private val contactEmail: String) : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> {
            return Observable.just(Action.Wish(Wish.LoadContact(contactEmail)))
        }

    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? =
            when (effect) {
                is Effect.ErrorOnContactLoad -> News.ContactLoadError(effect.throwable)
                else -> null
            }
    }
}