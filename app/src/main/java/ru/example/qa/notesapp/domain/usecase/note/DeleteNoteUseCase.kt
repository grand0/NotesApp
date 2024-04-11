package ru.example.qa.notesapp.domain.usecase.note

import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(note: NoteModel) {
        noteRepository.delete(note)
    }
}