package ru.example.qa.notesapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.example.qa.notesapp.data.local.db.AppDatabase
import ru.example.qa.notesapp.data.local.db.entity.UserEntity
import ru.example.qa.notesapp.data.local.db.entity.mapper.UserDomainModelMapper
import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.repository.UserRepository
import ru.example.qa.notesapp.util.PasswordEncoder
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val userDomainModelMapper: UserDomainModelMapper,
    private val passwordEncoder: PasswordEncoder,
) : UserRepository {

    override suspend fun getUserByCredentials(username: String, password: String): UserModel? {
        return withContext(Dispatchers.IO) {
            val entity = db.userDao.getUserByUsername(username)
            if (entity != null && passwordEncoder.check(password, entity.password)) {
                userDomainModelMapper.dataToDomain(entity)
            } else {
                null
            }
        }
    }

    override suspend fun getUserByRawCredentials(
        username: String,
        passwordHash: String,
    ): UserModel? {
        return withContext(Dispatchers.IO) {
            val entity = getUserEntityByRawCredentials(username, passwordHash)
            userDomainModelMapper.dataToDomain(entity)
        }
    }

    private suspend fun getUserEntityByRawCredentials(
        username: String,
        passwordHash: String,
    ): UserEntity? {
        val user = db.userDao.getUserByUsername(username)
        return if (user?.password == passwordHash) user else null
    }

    override suspend fun checkUsernameUnique(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            db.userDao.getUserByUsername(username) == null
        }
    }

    override suspend fun checkEmailUnique(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            db.userDao.getUserByEmail(email) == null
        }
    }

    override suspend fun saveUser(user: UserModel): UserModel {
        return withContext(Dispatchers.IO) {
            val password = user.password ?: throw NullPointerException("UserModel's password must not be null")
            val savedUser = user.copy(
                password = passwordEncoder.hash(password)
            )
            savedUser.id = db.userDao.saveUser(userDomainModelMapper.domainToData(savedUser)).toInt()
            savedUser
        }
    }
}