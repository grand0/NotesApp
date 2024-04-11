package ru.example.qa.notesapp.domain.model

import android.net.Uri
import java.io.Serializable
import java.util.Date

data class NoteModel(
    var id: Int = 0,
    var authorId: Int,
    var title: String? = null,
    var content: String? = null,
    var fileUri: String? = null,
    var lastEditTime: Date = Date(),
) : Serializable