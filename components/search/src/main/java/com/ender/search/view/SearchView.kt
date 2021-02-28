package com.ender.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.ender.search.R
import com.ender.search.databinding.ViewSearchBinding
import com.ender.search.view.SearchView.*
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface SearchView : RibView, ObservableSource<Event>, Consumer<ViewModel> {

    sealed class Event {
        data class KeywordInserted(val keyword: String) : Event()
    }

    interface Dependency {
        val queryHint: String
    }

    data class ViewModel(
        val isKeywordValid: Boolean
    )

    interface Factory : ViewFactory<Dependency, SearchView>

}

internal class SearchViewImpl(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create(),
    private val queryHint: String
) : AndroidRibView(), SearchView, ObservableSource<Event> by events, Consumer<ViewModel> {

    private var binding = ViewSearchBinding.bind(androidView)
    private val searchFieldText: EditText by lazy {
        binding.search.findViewById(R.id.search_src_text)
    }

    init {
        setUpSearchView()
    }

    override fun accept(viewModel: ViewModel) {
        handleSearchViewState(viewModel.isKeywordValid)
    }

    private fun handleSearchViewState(isKeywordValid: Boolean) {
        val textColor = R.color.search_field_text_color.takeIf { isKeywordValid }
            ?: R.color.search_field_invalid_text_color
        searchFieldText.setTextColor(androidView.context.resources.getColor(textColor))
    }

    private fun setUpSearchView() {
        with(binding.search) {
            this.queryHint = this@SearchViewImpl.queryHint
            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.run { events.accept(Event.KeywordInserted(newText)) }
                    return true
                }
            })
        }
    }

    class Factory(
        @LayoutRes private val layoutResourceId: Int = R.layout.view_search
    ) : SearchView.Factory {
        override fun invoke(dependency: Dependency): (RibView) -> SearchView = {
            SearchViewImpl(
                LayoutInflater.from(it.context)
                    .inflate(layoutResourceId, it.androidView, false) as ViewGroup,
                queryHint = dependency.queryHint
            )
        }
    }

}