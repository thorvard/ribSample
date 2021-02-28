package com.ender.search.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.ender.search.R
import com.ender.search.Search
import com.ender.search.SearchNode
import com.ender.search.feature.SearchFeature
import com.ender.search.interactor.KeywordValidator
import com.ender.search.interactor.SearchInteractor
import com.ender.search.view.SearchView

class SearchBuilder(
    private val dependency: Search.Dependency
) : SimpleBuilder<Search>() {

    private val queryHint: String by lazy {
        dependency.hint
            ?: dependency.context.getString(R.string.search_field_placeholder)
    }

    override fun build(buildParams: BuildParams<Nothing?>): Search {

        val keywordValidator = KeywordValidator()
        val customisation = buildParams.getOrDefault(Search.Customisation())
        val feature = SearchFeature(keywordValidator)
        val interactor = SearchInteractor(buildParams, feature)

        return SearchNode(
            buildParams = buildParams,
            customisation.viewFactory(object : SearchView.Dependency {
                override val queryHint: String = this@SearchBuilder.queryHint
            }),
            plugins = listOf(interactor)
        )
    }

}