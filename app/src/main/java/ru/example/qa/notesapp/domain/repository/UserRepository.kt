package ru.example.qa.notesapp.domain.repository

import ru.example.qa.notesapp.domain.model.UserModel

interface UserRepository {

    suspend fun getUserByCredentials(username: String, password: String): UserModel?
    suspend fun getUserByRawCredentials(username: String, passwordHash: String): UserModel?

    suspend fun checkUsernameUnique(username: String): Boolean
    suspend fun checkEmailUnique(email: String): Boolean
    suspend fun saveUser(user: UserModel): UserModel
}