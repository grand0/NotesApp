package ru.example.qa.notesapp.presentation.screens.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentAuthBinding
import ru.example.qa.notesapp.presentation.exception.auth.AuthBadCredentialsException
import ru.example.qa.notesapp.presentation.exception.auth.AuthPasswordRequiredException
import ru.example.qa.notesapp.presentation.exception.auth.AuthUsernameRequiredException
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.clearErrorOnTextChanged
import ru.example.qa.notesapp.util.observe
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    private val viewModel: AuthViewModel by viewModels()

    @Inject lateinit var navigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        initViewModelObservers()
    }

    private fun initUi() {
        with (binding) {
            tilUsername.clearErrorOnTextChanged()
            tilPassword.clearErrorOnTextChanged()

            btnLogin.setOnClickListener {
                val username = (etUsername.text ?: "").toString()
                val password = (etPassword.text ?: "").toString()
                viewModel.auth(username, password)
            }

            btnToRegister.setOnClickListener {
                navigateToRegisterScreen()
            }
        }
    }

    private fun initViewModelObservers() {
        with (viewModel) {
            authErrorsChannel.observe(this@AuthFragment) { err ->
                when (err) {
                    is AuthBadCredentialsException -> Snackbar.make(
                        binding.root,
                        R.string.wrong_credentials_error_text,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    is AuthUsernameRequiredException -> binding.tilUsername.error =
                        getString(R.string.required_error_text)

                    is AuthPasswordRequiredException -> binding.tilPassword.error =
                        getString(R.string.required_error_text)
                }
            }

            authState.observe(this@AuthFragment) { state ->
                when (state) {
                    AuthState.NOT_AUTHENTICATED -> switchLoading(false)
                    AuthState.AUTHENTICATING -> switchLoading(true)
                    AuthState.AUTHENTICATED -> navigateToMainScreen()
                }
            }
        }
    }

    private fun switchLoading(loading: Boolean) {
        with (binding) {
            tilUsername.isEnabled = !loading
            tilPassword.isEnabled = !loading
            btnLogin.isEnabled = !loading
            btnToRegister.isEnabled = !loading
        }
    }

    private fun navigateToRegisterScreen() {
        val action = AuthFragmentDirections.actionAuthFragmentToRegisterFragment()
        navigator.navigate(action)
    }

    private fun navigateToMainScreen() {
        val action = AuthFragmentDirections.actionAuthFragmentToMainFragment()
        navigator.navigate(action)
    }
}