package com.ender.ribs.contacts.list.router

import com.ender.ribs.contacts.list.ContactsList
import com.ender.ribs.contacts.list.R
import com.ender.search.Search
import com.ender.search.builder.SearchBuilder

class ParentChildBuilders(
    dependency: ContactsList.Dependency
) {

    private val subtreeDependency = SubtreeDependency(dependency)

    val searchViewBuilder: SearchBuilder = SearchBuilder(subtreeDependency)

    class SubtreeDependency(
        dependency: ContactsList.Dependency
    ) : ContactsList.Dependency by dependency, Search.Dependency {
        override val hint: String =
            dependency.context.resources.getString(R.string.search_field_placeholder)
    }
}
