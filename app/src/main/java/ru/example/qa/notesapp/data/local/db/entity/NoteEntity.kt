package ru.example.qa.notesapp.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(defaultValue = "NULL")
    var title: String?,
    @ColumnInfo(defaultValue = "NULL")
    var content: String?,
    @ColumnInfo(defaultValue = "NULL")
    var fileId: String?,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var lastEditTime: Long,
)