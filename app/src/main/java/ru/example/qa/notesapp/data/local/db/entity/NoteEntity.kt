package ru.example.qa.notesapp.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(index = true)
    var authorId: Int,
    @ColumnInfo(defaultValue = "NULL")
    var title: String?,
    @ColumnInfo(defaultValue = "NULL")
    var content: String?,
    @ColumnInfo(defaultValue = "NULL")
    var fileId: String?,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var lastEditTime: Long,
)