package ru.example.qa.notesapp.domain.usecase.note

import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllNotesOfUserUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(author: UserModel): List<NoteModel> {
        return noteRepository.getAllOfUser(author)
    }
}