package com.example.avitotech.data.extensions

import com.example.avitotech.domain.models.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomainUser(): User {
    return User(
        id = uid,
        email = email ?: "",
        name = displayName,
        profilePicture = photoUrl?.toString(),
        phoneNumber = phoneNumber
    )
}