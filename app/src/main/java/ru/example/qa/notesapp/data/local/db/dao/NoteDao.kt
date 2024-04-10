package ru.example.qa.notesapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import ru.example.qa.notesapp.data.local.db.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteEntity>
}