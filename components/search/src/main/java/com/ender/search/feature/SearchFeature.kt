package com.ender.search.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.ender.search.interactor.KeywordValidator
import io.reactivex.Observable

internal class SearchFeature(
    keywordValidator: KeywordValidator
) : ActorReducerFeature<SearchFeature.Wish, SearchFeature.Effect, SearchFeature.State, Nothing>(
    initialState = State(false, ""),
    actor = ActorImpl(keywordValidator),
    reducer = ReducerImpl()
) {

    data class State(val isKeywordValid: Boolean, val keyword: String)

    sealed class Wish {
        data class SearchKeyword(val keyword: String) : Wish()
    }

    sealed class Effect {
        data class SearchKeywordValid(val keyword: String) : Effect()
        data class SearchKeywordInValid(val keyword: String) : Effect()
    }

    private class ActorImpl(private val keywordValidator: KeywordValidator) :
        Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> =
            when (action) {
                is Wish.SearchKeyword -> Observable.just(
                    if (!keywordValidator.validate(action.keyword)) {
                        Effect.SearchKeywordInValid(action.keyword)
                    } else {
                        Effect.SearchKeywordValid(action.keyword)
                    }
                )
            }
    }

    private class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.SearchKeywordValid -> state.copy(
                    isKeywordValid = true,
                    keyword = effect.keyword
                )
                is Effect.SearchKeywordInValid -> state.copy(
                    isKeywordValid = false,
                    keyword = effect.keyword
                )
            }
    }
}