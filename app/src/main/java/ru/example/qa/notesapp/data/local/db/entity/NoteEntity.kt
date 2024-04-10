package ru.example.qa.notesapp.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String?,
    var content: String?,
    var fileId: String?,
    var lastEditTime: Long,
)