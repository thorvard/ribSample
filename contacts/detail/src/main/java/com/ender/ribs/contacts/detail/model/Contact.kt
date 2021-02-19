package com.ender.ribs.contacts.detail.model

data class Contact(
    val name: String,
    val emailAddress: String,
    val isFavourite: Boolean = false,
    val phoneNumber: String,
    val avatarUrl: String
)