package com.ender.ribs

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.ender.ribs.contacts.container.Container
import com.ender.ribs.contacts.container.builder.ContainerBuilder
import com.ender.ribs.storage.LocalStorage
import com.ender.ribs.storage.LocalStorageFactory

class MainActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        ContainerBuilder(
            object : Container.Dependency {
                override val localStorage: LocalStorage = LocalStorageFactory.localStorage
            }
        ).build(root(savedInstanceState))
}