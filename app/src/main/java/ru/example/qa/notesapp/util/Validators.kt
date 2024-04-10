package ru.example.qa.notesapp.util

import android.util.Patterns

object Validators {
    fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}