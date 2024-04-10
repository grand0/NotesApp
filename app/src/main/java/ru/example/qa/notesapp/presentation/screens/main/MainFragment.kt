package ru.example.qa.notesapp.presentation.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentMainBinding
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.observe
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var navigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initViewModelObservers()
        if (viewModel.userState.value == null) {
            viewModel.auth()
        }
    }

    private fun initUI() {

    }

    private fun initViewModelObservers() {
        with (viewModel) {
            authState.observe(this@MainFragment) { state ->
                when (state) {
                    AuthState.NOT_AUTHENTICATED -> navigateToAuthScreen()
                    AuthState.AUTHENTICATING -> switchLoading(true)
                    AuthState.AUTHENTICATED -> switchLoading(false)
                }
            }

            userState.observe(this@MainFragment) { user ->
                binding.tvUser.text = user?.username ?: ""
            }
        }
    }

    private fun switchLoading(loading: Boolean) {
        with (binding) {
            tvUser.isVisible = !loading
            piLoading.isVisible = loading
        }
    }

    private fun navigateToAuthScreen() {
        val action = MainFragmentDirections.actionMainFragmentToAuthFragment()
        navigator.navController.navigate(action)
    }
}