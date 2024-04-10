package ru.example.qa.notesapp.util

interface PasswordEncoder {
    fun hash(password: String): String
    fun check(plain: String, hashed: String): Boolean
}