package ru.example.qa.notesapp.domain.model

import java.io.Serializable
import java.util.Date

data class NoteModel(
    var id: Int = 0,
    var title: String? = null,
    var content: String? = null,
    var fileId: String? = null,
    var lastEditTime: Date = Date(),
) : Serializable