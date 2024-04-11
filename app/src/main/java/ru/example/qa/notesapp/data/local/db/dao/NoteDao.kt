package ru.example.qa.notesapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import ru.example.qa.notesapp.data.local.db.entity.NoteEntity
import java.util.Date

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteEntity>

    @Query("INSERT INTO notes DEFAULT VALUES")
    suspend fun createEmptyNote(): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int)
}