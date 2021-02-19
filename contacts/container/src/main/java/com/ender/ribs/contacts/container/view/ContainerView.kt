package com.ender.ribs.contacts.container.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.ender.ribs.contacts.container.R

interface ContainerView : RibView {

    interface Factory : ViewFactory<Nothing?, ContainerView>

}

class ContainerAndroidView(
    override val androidView: ViewGroup
) : AndroidRibView(), ContainerView {
    private val contentContainer: ViewGroup =
        androidView.findViewById(R.id.children_container)

    override fun getParentViewForChild(subtreeOf: Node<*>): ViewGroup =
        contentContainer

    class Factory(
        @LayoutRes private val layoutResourceId: Int = R.layout.contact_container
    ) : ContainerView.Factory {
        override fun invoke(p1: Nothing?): (RibView) -> ContainerView = {
            ContainerAndroidView(
                LayoutInflater.from(it.context)
                    .inflate(layoutResourceId, it.androidView, false) as ViewGroup
            )
        }
    }

}