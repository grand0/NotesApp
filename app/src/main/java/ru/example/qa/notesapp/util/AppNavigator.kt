package ru.example.qa.notesapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections

interface AppNavigator {
    val navController: NavController

    fun initNavController(hostFragmentId: Int): NavController

    fun navigate(directions: NavDirections) {
        navController.navigate(directions)
    }

    fun <T> observeCurrentBackStackEntryForResult(key: String, owner: LifecycleOwner, observer: Observer<in T>) {
        navController
            .currentBackStackEntry
            ?.savedStateHandle
            ?.remove<T>(key)
        navController
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<T>(key)
            ?.observe(owner, observer)
    }

    fun <T> setResult(key: String, value: T?) {
        navController
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(key, value)
    }
}
