package ru.example.qa.notesapp.util

import androidx.navigation.NavController

interface AppNavigator {
    val navController: NavController

    fun initNavController(hostFragmentId: Int): NavController
}