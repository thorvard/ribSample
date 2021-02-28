package com.ender.ribs.contacts.list.router

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.ender.ribs.contacts.list.router.ContainerRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class ContainerRouter constructor(
    buildParams: BuildParams<Nothing?>,
    private val childBuilders: ParentChildBuilders
) : Router<ContainerRouter.Configuration>(
    buildParams = buildParams,
    routingSource = RoutingSource.permanent(Permanent.SearchView)
) {

    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object SearchView : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        with(childBuilders) {
            when (routing.configuration) {
                is Permanent.SearchView -> attach { searchViewBuilder.build(it) }
            }
        }
}