package ru.example.qa.notesapp.data.local.db.entity.mapper

import android.net.Uri
import ru.example.qa.notesapp.data.local.db.entity.NoteEntity
import ru.example.qa.notesapp.data.local.storage.AppStorageManager
import ru.example.qa.notesapp.domain.model.NoteModel
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDomainModelMapper @Inject constructor(
    private val appStorageManager: AppStorageManager,
) {

    fun dataToDomain(entity: NoteEntity?): NoteModel? {
        if (entity == null) {
            return null
        }
        return NoteModel(
            id = entity.id,
            authorId = entity.authorId,
            title = entity.title,
            content = entity.content,
            fileUri = entity.fileId?.let { appStorageManager.getFileUri(it).toString() },
            lastEditTime = Date(entity.lastEditTime)
        )
    }

    fun domainToData(model: NoteModel): NoteEntity {
        return NoteEntity(
            id = model.id,
            authorId = model.authorId,
            title = model.title,
            content = model.content,
            fileId = model.fileUri?.let { appStorageManager.saveToAppStorage(Uri.parse(it)) },
            lastEditTime = model.lastEditTime.time,
        )
    }
}