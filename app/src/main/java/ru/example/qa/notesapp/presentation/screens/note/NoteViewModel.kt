package ru.example.qa.notesapp.presentation.screens.note

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.usecase.note.DeleteNoteUseCase
import ru.example.qa.notesapp.domain.usecase.note.UpdateNoteUseCase
import ru.example.qa.notesapp.data.local.storage.AppStorageManager

@HiltViewModel(assistedFactory = NoteViewModel.Factory::class)
class NoteViewModel @AssistedInject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    @Assisted private val note: NoteModel,
) : ViewModel() {

    private val _noteState = MutableSharedFlow<NoteModel>(replay = 1)
    val noteState = _noteState.asSharedFlow()

    private val _editingState = MutableStateFlow(false)
    val editingState = _editingState.asStateFlow()

    init {
        viewModelScope.launch {
            _noteState.emit(note)
        }
    }

    fun startEditing() {
        viewModelScope.launch {
            _editingState.value = true
        }
    }

    fun saveNote(title: String?, content: String?) {
        viewModelScope.launch {
            val oldNote = _noteState.replayCache[0]
            val editedNote = oldNote.copy(title = title, content = content)
            if (oldNote != editedNote) {
                _noteState.emit(updateNoteUseCase(editedNote))
            }
            stopEditing()
        }
    }

    fun cancelEdit() {
        viewModelScope.launch {
            _noteState.emit(_noteState.replayCache[0])
            stopEditing()
        }
    }

    private fun stopEditing() {
        viewModelScope.launch {
            _editingState.value = false
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            deleteNoteUseCase(_noteState.replayCache[0])
        }
    }

    fun attachImage(uri: Uri) {
        viewModelScope.launch {
            val newNote = _noteState.replayCache[0].copy(fileUri = uri.toString())
            _noteState.emit(updateNoteUseCase(newNote))
        }
    }

    fun detachImage() {
        val note = _noteState.replayCache[0]
        if (note.fileUri != null) {
            viewModelScope.launch {
                val newNote = note.copy(fileUri = null)
                _noteState.emit(updateNoteUseCase(newNote))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(note: NoteModel): NoteViewModel
    }
}