package ru.example.qa.notesapp.domain.model

data class UserModel(
    var id: Int = 0,
    var username: String,
    var email: String,
    var password: String? = null,
)
