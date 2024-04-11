package ru.example.qa.notesapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.example.qa.notesapp.data.local.db.AppDatabase
import ru.example.qa.notesapp.data.local.db.entity.mapper.NoteDomainModelMapper
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.repository.NoteRepository
import java.util.Date
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val noteDomainModelMapper: NoteDomainModelMapper,
) : NoteRepository {

    override suspend fun getAllOfUser(author: UserModel): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            db.noteDao.getAllOfUser(author.id).mapNotNull(noteDomainModelMapper::dataToDomain)
        }
    }

    override suspend fun createEmptyNote(author: UserModel): NoteModel {
        return withContext(Dispatchers.IO) {
            val id = db.noteDao.createEmptyNote(author.id).toInt()
            NoteModel(
                id = id,
                authorId = author.id,
            )
        }
    }

    override suspend fun update(note: NoteModel) {
        withContext(Dispatchers.IO) {
            db.noteDao.update(noteDomainModelMapper.domainToData(note.copy(lastEditTime = Date())))
        }
    }

    override suspend fun delete(note: NoteModel) {
        withContext(Dispatchers.IO) {
            db.noteDao.delete(note.id)
        }
    }
}