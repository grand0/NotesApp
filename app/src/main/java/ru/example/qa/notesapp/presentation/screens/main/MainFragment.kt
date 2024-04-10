package ru.example.qa.notesapp.presentation.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentMainBinding
import ru.example.qa.notesapp.presentation.adapter.NoteListAdapter
import ru.example.qa.notesapp.presentation.decoration.HorizontalMarginDecorator
import ru.example.qa.notesapp.presentation.decoration.VerticalMarginDecorator
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.observe
import ru.example.qa.notesapp.util.toPx
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var navigator: AppNavigator
    private var adapter: NoteListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initViewModelObservers()
        if (viewModel.userState.value == null) {
            viewModel.auth()
        }
    }

    private fun initUI() {
        with (binding) {
            val layoutManager = LinearLayoutManager(requireContext())
            adapter = NoteListAdapter()
            rvNotes.layoutManager = layoutManager
            rvNotes.adapter = adapter
            rvNotes.addItemDecoration(HorizontalMarginDecorator(offset = requireContext().toPx(8)))
            rvNotes.addItemDecoration(VerticalMarginDecorator(offset = requireContext().toPx(4)))

            toolbar.title = getString(R.string.notes_text)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_logout -> {
                        viewModel.logout()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun initViewModelObservers() {
        with (viewModel) {
            authState.observe(this@MainFragment) { state ->
                when (state) {
                    AuthState.NOT_AUTHENTICATED -> navigateToAuthScreen()
                    AuthState.AUTHENTICATING -> switchLoading(true)
                    AuthState.AUTHENTICATED -> { /* no-op */ }
                }
            }

            userState.observe(this@MainFragment) { user ->
                if (user != null) {
                    updateNotes()
                }
            }

            notesState.observe(this@MainFragment) { notes ->
                if (notes == null) {
                    switchLoading(true)
                } else {
                    switchLoading(false)
                    adapter?.submitList(notes)
                    if (notes.isEmpty()) {
                        showEmptyListMessage()
                    }
                }
            }
        }
    }

    private fun switchLoading(loading: Boolean) {
        with (binding) {
            rvNotes.isVisible = !loading
            piLoading.isVisible = loading
            tvEmptyListMsg.isVisible = false
        }
    }

    private fun showEmptyListMessage() {
        with (binding) {
            rvNotes.isVisible = false
            tvEmptyListMsg.isVisible = true
            piLoading.isVisible = false
        }
    }

    private fun navigateToAuthScreen() {
        val action = MainFragmentDirections.actionMainFragmentToAuthFragment()
        navigator.navController.navigate(action)
    }
}