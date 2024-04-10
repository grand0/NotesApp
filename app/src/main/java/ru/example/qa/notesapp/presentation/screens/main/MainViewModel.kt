package ru.example.qa.notesapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.session.AppSession
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSession: AppSession,
) : ViewModel() {

    private val _userState = MutableStateFlow<UserModel?>(null)
    val userState = _userState.asStateFlow()

    private val _authState = MutableStateFlow(AuthState.AUTHENTICATING)
    val authState = _authState.asStateFlow()

    fun auth() {
        viewModelScope.launch {
            if (appSession.authorizedUser == null) {
                appSession.restoreSession()
                if (appSession.authorizedUser == null) {
                    _userState.value = null
                    _authState.value = AuthState.NOT_AUTHENTICATED
                    return@launch
                }
            }
            _userState.value = appSession.authorizedUser
            _authState.value = AuthState.AUTHENTICATED
        }
    }

    fun logout() {
        viewModelScope.launch {
            appSession.clearSession()
            _authState.value = AuthState.NOT_AUTHENTICATED
        }
    }
}