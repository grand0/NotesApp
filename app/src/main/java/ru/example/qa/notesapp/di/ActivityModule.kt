package ru.example.qa.notesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.AppNavigatorImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    @Binds
    @ActivityScoped
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}