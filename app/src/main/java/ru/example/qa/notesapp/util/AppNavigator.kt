package ru.example.qa.notesapp.util

import androidx.navigation.NavController
import androidx.navigation.NavDirections

interface AppNavigator {
    val navController: NavController

    fun initNavController(hostFragmentId: Int): NavController

    fun navigate(directions: NavDirections) {
        navController.navigate(directions)
    }
}