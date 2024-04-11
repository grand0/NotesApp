package ru.example.qa.notesapp.presentation.dialog

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.DialogDeleteConfirmationBinding
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.Keys
import javax.inject.Inject

@AndroidEntryPoint
class NoteDeleteConfirmationBottomSheetDialog : BottomSheetDialogFragment(R.layout.dialog_delete_confirmation) {

    private val binding by viewBinding(DialogDeleteConfirmationBinding::bind)
    @Inject lateinit var navigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (binding) {
            btnDelete.setOnClickListener {
                navigator.setResult(Keys.NOTE_DELETE_CONFIRMATION_DIALOG_RESULT_KEY, true)
                dismiss()
            }
            btnCancel.setOnClickListener {
                navigator.setResult(Keys.NOTE_DELETE_CONFIRMATION_DIALOG_RESULT_KEY, false)
                dismiss()
            }
        }
    }
}