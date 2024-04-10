package ru.example.qa.notesapp.util

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val activity: FragmentActivity,
) : AppNavigator {

    override lateinit var navController: NavController
        private set

    override fun initNavController(hostFragmentId: Int): NavController {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(hostFragmentId) as NavHostFragment
        navController = navHostFragment.navController
        return navController
    }
}