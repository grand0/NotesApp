package ru.example.qa.notesapp.presentation.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentMainBinding
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.presentation.adapter.NoteListAdapter
import ru.example.qa.notesapp.presentation.decoration.HorizontalMarginDecorator
import ru.example.qa.notesapp.presentation.decoration.VerticalMarginDecorator
import ru.example.qa.notesapp.presentation.model.AuthState
import ru.example.qa.notesapp.presentation.model.NoteMenuResult
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.Keys
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
            adapter = NoteListAdapter(
                onItemClickListener = ::navigateToNoteScreen,
                onItemMenuClickListener = ::openNoteMenuDialog
            ).apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            rvNotes.layoutManager = layoutManager
            rvNotes.adapter = adapter
            rvNotes.addItemDecoration(HorizontalMarginDecorator(offset = requireContext().toPx(8)))
            rvNotes.addItemDecoration(VerticalMarginDecorator(offset = requireContext().toPx(4)))

            registerForContextMenu(rvNotes)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_logout -> {
                        viewModel.logout()
                        true
                    }
                    else -> false
                }
            }

            fabAdd.setOnClickListener {
                viewModel.newNote()
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

            newNoteState.observe(this@MainFragment) { newNote ->
                if (newNote != null) {
                    consumedNewNote()
                    navigateToNoteScreen(newNote, editMode = true)
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
        with(binding) {
            rvNotes.isVisible = false
            tvEmptyListMsg.isVisible = true
            piLoading.isVisible = false
        }
    }

    private fun openNoteMenuDialog(note: NoteModel) {
        navigator.observeCurrentBackStackEntryForResult<NoteMenuResult>(
            Keys.NOTE_MENU_DIALOG_RESULT_KEY,
            viewLifecycleOwner,
        ) { result ->
            when (result) {
                NoteMenuResult.EDIT -> {
                    navigator.navController.popBackStack(R.id.mainFragment, inclusive = false)
                    navigateToNoteScreen(note, editMode = true)
                }
                NoteMenuResult.DELETE -> {
                    navigator.navController.popBackStack(R.id.mainFragment, inclusive = false)
                    openDeleteConfirmationDialog(note)
                }
            }
        }
        val action = MainFragmentDirections.actionMainFragmentToNoteMenuBottomSheetDialog()
        navigator.navigate(action)
    }

    private fun openDeleteConfirmationDialog(note: NoteModel) {
        navigator.observeCurrentBackStackEntryForResult<Boolean>(
            Keys.NOTE_DELETE_CONFIRMATION_DIALOG_RESULT_KEY,
            viewLifecycleOwner
        ) { delete ->
            if (delete) {
                viewModel.deleteNote(note)
            }
        }
        val action = MainFragmentDirections.actionMainFragmentToNoteDeleteConfirmationBottomSheetDialog()
        navigator.navigate(action)
    }

    private fun navigateToAuthScreen() {
        val action = MainFragmentDirections.actionMainFragmentToAuthFragment()
        navigator.navigate(action)
    }

    private fun navigateToNoteScreen(note: NoteModel, editMode: Boolean = false) {
        val action = MainFragmentDirections.actionMainFragmentToNoteFragment(note, editMode)
        navigator.navigate(action)
    }
}