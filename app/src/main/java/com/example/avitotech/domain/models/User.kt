package com.example.avitotech.domain.models

data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val profilePicture: String? = null,
    val phoneNumber: String? = null
)