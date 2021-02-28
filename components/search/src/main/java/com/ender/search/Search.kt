package com.ender.search

import android.content.Context
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.ender.search.view.SearchView
import com.ender.search.view.SearchViewImpl

interface Search : Rib, Connectable<Nothing, Search.Output> {

    interface Dependency {
        val context: Context
        val hint: String?
    }

    sealed class Output {
        data class KeywordInserted(val keyword: String) : Output()
    }

    class Customisation(
        val viewFactory: SearchView.Factory = SearchViewImpl.Factory()
    ) : RibCustomisation

}