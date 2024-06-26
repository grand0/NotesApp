package ru.example.qa.notesapp.presentation.screens.note

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.FragmentNoteBinding
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.Constants
import ru.example.qa.notesapp.util.Keys
import ru.example.qa.notesapp.util.observe
import javax.inject.Inject


@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding by viewBinding(FragmentNoteBinding::bind)
    private val args: NoteFragmentArgs by navArgs()
    private val viewModel: NoteViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<NoteViewModel.Factory> { factory ->
                factory.create(args.note)
            }
        }
    )

    @Inject lateinit var navigator: AppNavigator
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.attachImage(uri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        initViewModelObservers()
        if (args.editMode) {
            viewModel.startEditing()
        }
    }

    private fun initUi() {
        with (binding) {
            btnAttach.setOnClickListener {
                pickImage()
            }
            btnClearAttachment.setOnClickListener {
                viewModel.detachImage()
            }

            etTitle.setText(args.note.title)
            etContent.setText(args.note.content)

            with (toolbar) {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_edit -> {
                            viewModel.startEditing()
                            true
                        }
                        R.id.action_delete -> {
                            openDeleteConfirmationDialog()
                            true
                        }
                        R.id.action_cancel -> {
                            viewModel.cancelEdit()
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }

    private fun initViewModelObservers() {
        with (viewModel) {
            noteState.observe(this@NoteFragment) { note ->
                with (binding) {
                    if (!editingState.value) {
                        etTitle.setText(note.title)
                        etContent.setText(note.content)
                    }
                    toolbar.subtitle = Constants.DATE_TIME_FORMATTER.format(note.lastEditTime)
                    setAttachmentControls(note)
                }
            }

            editingState.observe(this@NoteFragment, ::setEditMode)
        }
    }

    private fun pickImage() {
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setAttachmentControls(note: NoteModel) {
        with (binding) {
            if (note.fileUri == null) {
                btnClearAttachment.isVisible = false
                ivNoteAttachment.isVisible = false
                ivNoteAttachment.setImageDrawable(null)
            } else {
                btnClearAttachment.isVisible = true
                ivNoteAttachment.isVisible = true
                Glide.with(this@NoteFragment)
                    .load(note.fileUri)
                    .into(ivNoteAttachment)
            }
        }
    }

    private fun setEditMode(edit: Boolean) {
        with (binding) {
            etTitle.isFocusableInTouchMode = edit
            etTitle.isFocusable = edit
            etContent.isFocusableInTouchMode = edit
            etContent.isFocusable = edit

            with (toolbar) {
                navigationIcon = AppCompatResources.getDrawable(
                    requireContext(),
                    if (edit) R.drawable.ic_done else R.drawable.ic_back
                )
                if (edit) {
                    setNavigationOnClickListener {
                        val title = etTitle.text?.toString()
                        val content = etContent.text?.toString()
                        saveNote(title, content)
                    }
                } else {
                    setNavigationOnClickListener { navigateUp() }
                }
                menu.findItem(R.id.action_edit).isVisible = !edit
                menu.findItem(R.id.action_delete).isVisible = !edit
                menu.findItem(R.id.action_cancel).isVisible = edit
            }

            if (edit) {
                etContent.requestFocus()
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.showSoftInput(etContent, 0)
            } else {
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(etContent.windowToken, 0)
            }
        }
    }

    private fun openDeleteConfirmationDialog() {
        navigator.observeCurrentBackStackEntryForResult<Boolean>(
            Keys.NOTE_DELETE_CONFIRMATION_DIALOG_RESULT_KEY,
            viewLifecycleOwner
        ) { delete ->
            if (delete) {
                navigator.navController.popBackStack(R.id.noteFragment, inclusive = false)
                deleteNote()
            }
        }
        val action = NoteFragmentDirections.actionNoteFragmentToNoteDeleteConfirmationBottomSheetDialog()
        navigator.navigate(action)
    }

    private fun navigateUp() {
        navigator.navController.navigateUp()
    }

    private fun saveNote(title: String?, content: String?) {
        viewModel.saveNote(title, content)
    }

    private fun deleteNote() {
        viewModel.deleteNote()
        navigateUp()
    }
}