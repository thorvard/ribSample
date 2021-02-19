package com.ender.ribs.contacts.list.view

import android.view.View
import com.ender.ribs.contacts.list.R
import com.ender.ribs.contacts.list.feature.ContactsListFeature
import com.ender.ribs.contacts.list.feature.ContactsListFeature.News
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer

internal class NewsListener(
    private val view: View,
    private val feature: ContactsListFeature
) : Consumer<News> {

    override fun accept(news: News?) {
        news?.run {
            when (this) {
                is News.ContactLoadError -> renderContactLoadError()
                is News.GenericError -> renderGenericError()
            }
        }
    }

    private fun renderContactLoadError() {
        Snackbar
            .make(view, view.context.getString(R.string.error_load_contacts), Snackbar.LENGTH_LONG)
            .setAction(view.context.getString(R.string.action_retry)) {
                feature.accept(ContactsListFeature.Wish.LoadContacts)
            }.show()
    }

    private fun renderGenericError() {
        Snackbar
            .make(view, view.context.getString(R.string.error), Snackbar.LENGTH_SHORT)
            .show()
    }
}