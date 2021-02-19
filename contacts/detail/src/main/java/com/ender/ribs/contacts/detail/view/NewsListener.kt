package com.ender.ribs.contacts.detail.view

import android.view.View
import com.ender.ribs.contacts.detail.R
import com.ender.ribs.contacts.detail.feature.ContactDetailFeature
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer

private class NewsListener(
    private val view: View,
    private val feature: ContactDetailFeature
) : Consumer<ContactDetailFeature.News> {

    override fun accept(news: ContactDetailFeature.News?) {
        news?.run {
            when (this) {
                is ContactDetailFeature.News.ContactLoadError -> renderContactLoadError()
            }
        }
    }

    private fun renderContactLoadError() {
        Snackbar
            .make(view, view.context.getString(R.string.error_load_contact), Snackbar.LENGTH_LONG)
            .setAction(view.context.getString(R.string.action_retry)) {
//                feature.accept(ContactDetailFeature.Wish.LoadContacts)
            }.show()
    }

    private fun renderGenericError() {
        Snackbar
            .make(view, view.context.getString(R.string.error), Snackbar.LENGTH_SHORT)
            .show()
    }
}