package ru.example.qa.notesapp.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true),
    ]
)
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String,
    var email: String,
    var password: String,
)