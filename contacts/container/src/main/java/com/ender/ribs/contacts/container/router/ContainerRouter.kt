package com.ender.ribs.contacts.container.router

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.ender.ribs.contacts.detail.builder.ContactDetailBuilder
import kotlinx.android.parcel.Parcelize

class ContainerRouter constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val childBuilders: ParentChildBuilders
) : Router<ContainerRouter.Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object List : Configuration()

            @Parcelize
            data class Detail(val emailAddress: String) : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        with(childBuilders) {
            when (val configuration = routing.configuration) {
                is Configuration.Content.List -> attach { contactList.build(it) }
                is Configuration.Content.Detail -> attach {
                    contactDetail.build(
                        it,
                        ContactDetailBuilder.Params(configuration.emailAddress)
                    )
                }
            }
        }
}