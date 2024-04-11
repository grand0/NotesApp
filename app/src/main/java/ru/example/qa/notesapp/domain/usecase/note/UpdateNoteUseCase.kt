package ru.example.qa.notesapp.domain.usecase.note

import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(note: NoteModel): NoteModel {
        return noteRepository.update(note)
    }
}