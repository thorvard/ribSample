package com.ender.ribs.storage.model

data class ContactEntity(
    val first: String,
    val last: String,
    val email: String,
    val phone: String,
    val thumbnail: String,
    val imageLarge: String,
    val isFavourite: Boolean
)
