package ru.example.qa.notesapp.presentation.dialog

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.example.qa.notesapp.R
import ru.example.qa.notesapp.databinding.DialogNoteMenuBinding
import ru.example.qa.notesapp.presentation.model.NoteMenuResult
import ru.example.qa.notesapp.util.AppNavigator
import ru.example.qa.notesapp.util.Keys
import javax.inject.Inject

@AndroidEntryPoint
class NoteMenuBottomSheetDialog : BottomSheetDialogFragment(R.layout.dialog_note_menu) {

    private val binding by viewBinding(DialogNoteMenuBinding::bind)
    @Inject lateinit var navigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (binding) {
            btnEdit.setOnClickListener {
                navigator.setResult(Keys.NOTE_MENU_DIALOG_RESULT_KEY, NoteMenuResult.EDIT)
                dismiss()
            }
            btnDelete.setOnClickListener {
                navigator.setResult(Keys.NOTE_MENU_DIALOG_RESULT_KEY, NoteMenuResult.DELETE)
                dismiss()
            }
        }
    }
}