package com.ender.ribs.contacts.container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.ender.ribs.contacts.container.Container
import com.ender.ribs.contacts.container.interactor.ContainerInteractor
import com.ender.ribs.contacts.container.ContainerNode
import com.ender.ribs.contacts.container.router.ParentChildBuilders
import com.ender.ribs.contacts.container.router.ContainerRouter

class ContainerBuilder(
    private val dependency: Container.Dependency
) : SimpleBuilder<Container>() {

    private val builders by lazy { ParentChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Container {

        val customisation = buildParams.getOrDefault(Container.Customisation())
        val backStack = backStack(buildParams)
        val router = ContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            childBuilders = builders
        )

        val interactor = ContainerInteractor(
            buildParams = buildParams,
            backStack = backStack
        )

        return ContainerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                router,
                interactor
            )
        )
    }


    private fun backStack(buildParams: BuildParams<Nothing?>): BackStackFeature<ContainerRouter.Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = ContainerRouter.Configuration.Content.List
        )
}