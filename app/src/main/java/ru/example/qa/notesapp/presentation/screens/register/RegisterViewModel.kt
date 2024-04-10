package ru.example.qa.notesapp.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.qa.notesapp.domain.usecase.user.CheckEmailUniqueUseCase
import ru.example.qa.notesapp.domain.usecase.user.CheckUsernameUniqueUseCase
import ru.example.qa.notesapp.domain.usecase.user.SaveUserUseCase
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationEmailNotUniqueException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationEmailRequiredException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationInvalidEmailException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationPasswordRequiredException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationUsernameNotUniqueException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationUsernameRequiredException
import ru.example.qa.notesapp.presentation.model.RegistrationState
import ru.example.qa.notesapp.session.AppSession
import ru.example.qa.notesapp.util.Validators
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val checkUsernameUniqueUseCase: CheckUsernameUniqueUseCase,
    private val checkEmailUniqueUseCase: CheckEmailUniqueUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val appSession: AppSession,
) : ViewModel() {

    private val _registrationState = MutableStateFlow(RegistrationState.NOT_REGISTERED)
    val registrationState = _registrationState.asStateFlow()

    val registrationErrorsChannel = Channel<RegistrationException>()

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.REGISTERING
            var valid = true
            if (username.isEmpty()) {
                registrationErrorsChannel.send(RegistrationUsernameRequiredException())
                valid = false
            } else if (!checkUsernameUniqueUseCase(username)) {
                registrationErrorsChannel.send(RegistrationUsernameNotUniqueException())
                valid = false
            }
            if (email.isEmpty()) {
                registrationErrorsChannel.send(RegistrationEmailRequiredException())
                valid = false
            } else if (!Validators.validateEmail(email)) {
                registrationErrorsChannel.send(RegistrationInvalidEmailException())
                valid = false
            } else if (!checkEmailUniqueUseCase(email)) {
                registrationErrorsChannel.send(RegistrationEmailNotUniqueException())
                valid = false
            }
            if (password.isEmpty()) {
                registrationErrorsChannel.send(RegistrationPasswordRequiredException())
                valid = false
            }

            if (valid) {
                val user = saveUserUseCase(username, email, password)
                appSession.authorizeUserByRawCredentials(user.username, user.password!!)
                _registrationState.value = RegistrationState.REGISTERED
            } else {
                _registrationState.value = RegistrationState.NOT_REGISTERED
            }
        }
    }

    override fun onCleared() {
        registrationErrorsChannel.close()
        super.onCleared()
    }
}