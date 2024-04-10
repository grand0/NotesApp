package ru.example.qa.notesapp.data.local.db.entity.mapper

import ru.example.qa.notesapp.data.local.db.entity.NoteEntity
import ru.example.qa.notesapp.domain.model.NoteModel
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDomainModelMapper @Inject constructor() {

    fun dataToDomain(entity: NoteEntity?): NoteModel? {
        if (entity == null) {
            return null
        }
        return NoteModel(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            fileId = entity.fileId,
            lastEditTime = Date(entity.lastEditTime)
        )
    }

    fun domainToData(model: NoteModel): NoteEntity {
        return NoteEntity(
            id = model.id,
            title = model.title,
            content = model.content,
            fileId = model.fileId,
            lastEditTime = model.lastEditTime.time,
        )
    }
}