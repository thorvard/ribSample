package com.ender.ribs.contacts.container.interactor

import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.source.backstack.operation.push
import com.ender.ribs.contacts.container.Container
import com.ender.ribs.contacts.container.router.ContainerRouter
import com.ender.ribs.contacts.container.view.ContainerView
import com.ender.ribs.contacts.list.ContactsList
import io.reactivex.functions.Consumer

class ContainerInteractor(
    buildParams: BuildParams<Nothing?>,
    private val backStack: BackStackFeature<ContainerRouter.Configuration>
) : Interactor<Container, ContainerView>(buildParams = buildParams) {

    private val contactsListListener = Consumer<ContactsList.Output> { output ->
        when (output) {
            is ContactsList.Output.ContactSelected -> backStack.push(
                ContainerRouter.Configuration.Content.Detail(output.contactEmail)
            )
        }
    }

    override fun onChildCreated(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is ContactsList -> {
                    bind(child.output to contactsListListener)
                }
            }
        }
    }
}