package ru.example.qa.notesapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.example.qa.notesapp.data.local.db.AppDatabase
import ru.example.qa.notesapp.data.repository.NoteRepositoryImpl
import ru.example.qa.notesapp.data.repository.UserRepositoryImpl
import ru.example.qa.notesapp.domain.repository.NoteRepository
import ru.example.qa.notesapp.domain.repository.UserRepository
import ru.example.qa.notesapp.util.Keys
import ru.example.qa.notesapp.util.Md5PasswordEncoder
import ru.example.qa.notesapp.util.PasswordEncoder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindPasswordEncoder(impl: Md5PasswordEncoder): PasswordEncoder

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "notesapp-db",
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(Keys.APP_SHARED_PREFS, Context.MODE_PRIVATE)
        }
    }
}