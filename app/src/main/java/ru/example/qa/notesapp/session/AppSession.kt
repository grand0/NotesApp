package ru.example.qa.notesapp.session

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.usecase.user.GetUserByCredentialsUseCase
import ru.example.qa.notesapp.domain.usecase.user.GetUserByRawCredentialsUseCase
import ru.example.qa.notesapp.util.Keys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSession @Inject constructor(
    private val prefs: SharedPreferences,
    private val getUserByRawCredentialsUseCase: GetUserByRawCredentialsUseCase,
    private val getUserByCredentialsUseCase: GetUserByCredentialsUseCase,
) {
    var authorizedUser: UserModel? = null
        private set

    suspend fun authorizeUserByCredentials(username: String, password: String, remember: Boolean = true): Boolean {
        authorizedUser = getUserByCredentialsUseCase(username, password)
        if (authorizedUser != null && remember) {
            authorizedUser?.password?.let { rememberUser(username, it) }
        }
        return authorizedUser != null
    }

    private suspend fun authorizeUserByRawCredentials(username: String, passwordHash: String, remember: Boolean = true): Boolean {
        authorizedUser = getUserByRawCredentialsUseCase(username, passwordHash)
        if (authorizedUser != null && remember) {
            rememberUser(username, passwordHash)
        }
        return authorizedUser != null
    }

    private fun rememberUser(username: String, passwordHash: String) {
        prefs.edit {
            putString(Keys.USER_USERNAME_PREF, username)
            putString(Keys.USER_PASSWORD_HASH_PREF, passwordHash)
        }
    }

    suspend fun restoreSession() {
        prefs.apply {
            val username = getString(Keys.USER_USERNAME_PREF, null)
            val password = getString(Keys.USER_PASSWORD_HASH_PREF, null)
            if (username != null && password != null) {
                authorizeUserByRawCredentials(username, password, remember = false)
            }
        }
    }

    fun clearSession() {
        authorizedUser = null
        prefs.edit {
            putString(Keys.USER_USERNAME_PREF, null)
            putString(Keys.USER_PASSWORD_HASH_PREF, null)
        }
    }
}