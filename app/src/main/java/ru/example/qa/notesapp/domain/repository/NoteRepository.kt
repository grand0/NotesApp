package ru.example.qa.notesapp.domain.repository

import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.model.UserModel

interface NoteRepository {

    suspend fun getAllOfUser(author: UserModel): List<NoteModel>
    suspend fun update(note: NoteModel): NoteModel
    suspend fun delete(note: NoteModel)
    suspend fun createEmptyNote(author: UserModel): NoteModel
}