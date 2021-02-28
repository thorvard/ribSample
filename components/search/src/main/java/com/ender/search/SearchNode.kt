package com.ender.search

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.ender.search.view.SearchView

internal class SearchNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> SearchView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Nothing, Search.Output> = NodeConnector()
) : Node<SearchView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Search, Connectable<Nothing, Search.Output> by connector