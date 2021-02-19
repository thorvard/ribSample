package com.ender.ribs.contacts.list.model

data class Contact(
    val name: String,
    val email: String,
    val isFavourite: Boolean = false,
    val phoneNumber: String,
    val avatarUrl: String
)