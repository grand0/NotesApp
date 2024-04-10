package ru.example.qa.notesapp.domain.repository

import ru.example.qa.notesapp.domain.model.NoteModel

interface NoteRepository {

    suspend fun getAll(): List<NoteModel>
}