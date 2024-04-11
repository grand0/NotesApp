package ru.example.qa.notesapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.qa.notesapp.data.local.db.dao.NoteDao
import ru.example.qa.notesapp.data.local.db.dao.UserDao
import ru.example.qa.notesapp.data.local.db.entity.NoteEntity
import ru.example.qa.notesapp.data.local.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        NoteEntity::class,
    ],
    version = 4,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val noteDao: NoteDao
}
