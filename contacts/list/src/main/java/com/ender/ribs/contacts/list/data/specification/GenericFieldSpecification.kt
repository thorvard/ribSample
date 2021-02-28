package com.ender.ribs.contacts.list.data.specification

import com.ender.ribs.storage.FilterSpecification
import com.ender.ribs.storage.model.ContactEntity

internal class GenericFieldSpecification(private val keyword: String) : FilterSpecification {
    override fun invoke(contactEntity: ContactEntity): Boolean = with(contactEntity) {
        first.startsWith(keyword, true) ||
                last.startsWith(keyword, true) ||
                email.startsWith(keyword, true)
    }
}