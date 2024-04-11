package ru.example.qa.notesapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.example.qa.notesapp.data.local.db.AppDatabase
import ru.example.qa.notesapp.data.local.db.entity.mapper.NoteDomainModelMapper
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val noteDomainModelMapper: NoteDomainModelMapper,
) : NoteRepository {

    override suspend fun getAll(): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            db.noteDao.getAll().mapNotNull(noteDomainModelMapper::dataToDomain)
        }
    }

    override suspend fun createEmptyNote(): NoteModel {
        return withContext(Dispatchers.IO) {
            val id = db.noteDao.createEmptyNote().toInt()
            NoteModel(id = id)
        }
    }

    override suspend fun update(note: NoteModel) {
        withContext(Dispatchers.IO) {
            db.noteDao.update(noteDomainModelMapper.domainToData(note))
        }
    }

    override suspend fun delete(note: NoteModel) {
        withContext(Dispatchers.IO) {
            db.noteDao.delete(note.id)
        }
    }
}