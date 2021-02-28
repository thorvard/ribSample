package com.ender.search.interactor

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.ender.search.Search
import com.ender.search.feature.SearchFeature
import com.ender.search.view.SearchView

internal class SearchInteractor(
    buildParams: BuildParams<Nothing?>,
    private val feature: SearchFeature,
) : Interactor<Search, SearchView>(buildParams) {

    override fun onViewCreated(view: SearchView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to rib.output using StateToOutputMapper)
            bind(feature to view using StateToViewModelMapper)
            bind(view to feature using ViewEventToWishMapper)
        }
    }
}

private object StateToOutputMapper : (SearchFeature.State) -> Search.Output? {
    override fun invoke(state: SearchFeature.State): Search.Output? =
        if (state.isKeywordValid) Search.Output.KeywordInserted(state.keyword)
        else null
}

private object StateToViewModelMapper : (SearchFeature.State) -> SearchView.ViewModel {
    override fun invoke(state: SearchFeature.State): SearchView.ViewModel =
        SearchView.ViewModel(state.isKeywordValid)
}

private object ViewEventToWishMapper : (SearchView.Event) -> SearchFeature.Wish {
    override fun invoke(event: SearchView.Event): SearchFeature.Wish =
        when (event) {
            is SearchView.Event.KeywordInserted -> SearchFeature.Wish.SearchKeyword(event.keyword)
        }
}