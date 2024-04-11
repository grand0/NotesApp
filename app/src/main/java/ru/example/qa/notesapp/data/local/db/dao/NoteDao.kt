package ru.example.qa.notesapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import ru.example.qa.notesapp.data.local.db.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE authorId = :authorId ORDER BY lastEditTime DESC")
    suspend fun getAllOfUser(authorId: Int): List<NoteEntity>

    @Query("INSERT INTO notes (authorId) VALUES (:authorId)")
    suspend fun createEmptyNote(authorId: Int): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int)
}