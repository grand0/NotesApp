package ru.example.qa.notesapp.presentation.screens.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentRegisterBinding
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationEmailNotUniqueException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationEmailRequiredException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationInvalidEmailException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationPasswordRequiredException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationUsernameNotUniqueException
import ru.example.qa.notesapp.presentation.exception.registration.RegistrationUsernameRequiredException
import ru.example.qa.notesapp.presentation.model.RegistrationState
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.clearErrorOnTextChanged
import ru.example.qa.notesapp.util.observe
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    private val viewModel: RegisterViewModel by viewModels()

    @Inject lateinit var navigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        initViewModelObservers()
    }

    private fun initUi() {
        with (binding) {
            tilUsername.clearErrorOnTextChanged()
            tilEmail.clearErrorOnTextChanged()
            tilPassword.clearErrorOnTextChanged()

            btnRegister.setOnClickListener {
                val username = (etUsername.text ?: "").toString()
                val email = (etEmail.text ?: "").toString()
                val password = (etPassword.text ?: "").toString()
                viewModel.register(username, email, password)
            }

            btnToLogin.setOnClickListener {
                navigateToLoginScreen()
            }
        }
    }

    private fun initViewModelObservers() {
        with (viewModel) {
            registrationErrorsChannel.observe(this@RegisterFragment) { err ->
                when (err) {
                    is RegistrationEmailNotUniqueException -> binding.tilEmail.error = getString(R.string.unique_error_text)
                    is RegistrationEmailRequiredException -> binding.tilEmail.error = getString(R.string.required_error_text)
                    is RegistrationInvalidEmailException -> binding.tilEmail.error = getString(R.string.invalid_email_error_text)
                    is RegistrationPasswordRequiredException -> binding.tilPassword.error = getString(R.string.required_error_text)
                    is RegistrationUsernameNotUniqueException -> binding.tilUsername.error = getString(R.string.unique_error_text)
                    is RegistrationUsernameRequiredException -> binding.tilUsername.error = getString(R.string.required_error_text)
                }
            }

            registrationState.observe(this@RegisterFragment) { state ->
                when (state) {
                    RegistrationState.NOT_REGISTERED -> switchLoading(false)
                    RegistrationState.REGISTERING -> switchLoading(true)
                    RegistrationState.REGISTERED -> navigateToMainScreen()
                }
            }
        }
    }

    private fun switchLoading(loading: Boolean) {
        with (binding) {
            tilUsername.isEnabled = !loading
            tilEmail.isEnabled = !loading
            tilPassword.isEnabled = !loading
            btnRegister.isEnabled = !loading
            btnToLogin.isEnabled = !loading
        }
    }

    private fun navigateToLoginScreen() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToAuthFragment()
        navigator.navController.navigate(action)
    }

    private fun navigateToMainScreen() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToMainFragment()
        navigator.navController.navigate(action)
    }
}