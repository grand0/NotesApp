package ru.example.qa.notesapp.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.qa.notesapp.domain.usecase.user.GetUserByCredentialsUseCase
import ru.example.qa.notesapp.presentation.exception.auth.AuthException
import ru.example.qa.notesapp.presentation.exception.auth.BadCredentialsException
import ru.example.qa.notesapp.presentation.exception.auth.PasswordRequiredException
import ru.example.qa.notesapp.presentation.exception.auth.UsernameRequiredException
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.session.AppSession
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getUserByCredentialsUseCase: GetUserByCredentialsUseCase,
    private val appSession: AppSession,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.NOT_AUTHENTICATED)
    val authState = _authState.asStateFlow()

    val authErrorsChannel = Channel<AuthException>()

    fun auth(username: String, password: String) {
        _authState.value = AuthState.AUTHENTICATING
        viewModelScope.launch {
            var valid = true
            if (username.isEmpty()) {
                authErrorsChannel.send(UsernameRequiredException())
                valid = false
            }
            if (password.isEmpty()) {
                authErrorsChannel.send(PasswordRequiredException())
                valid = false
            }

            if (valid) {
                val authed = appSession.authorizeUserByCredentials(username, password)
                if (authed) {
                    _authState.value = AuthState.AUTHENTICATED
                } else {
                    authErrorsChannel.send(BadCredentialsException())
                    _authState.value = AuthState.NOT_AUTHENTICATED
                }
            } else {
                _authState.value = AuthState.NOT_AUTHENTICATED
            }
        }
    }

    override fun onCleared() {
        authErrorsChannel.close()
        super.onCleared()
    }
}