package ru.example.qa.notesapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.qa.notesapp.data.local.db.dao.UserDao
import ru.example.qa.notesapp.data.local.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}
