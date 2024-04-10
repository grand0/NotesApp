package ru.example.qa.notesapp.data.local.db.entity.mapper

import ru.example.qa.notesapp.data.local.db.entity.UserEntity
import ru.example.qa.notesapp.domain.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDomainModelMapper @Inject constructor() {

    fun dataToDomain(entity: UserEntity?): UserModel? {
        if (entity == null) {
            return null
        }
        return UserModel(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            password = entity.password,
        )
    }

    fun domainToData(model: UserModel): UserEntity {
        return UserEntity(
            id = model.id,
            username = model.username,
            email = model.email,
            password = model.password!!,
        )
    }
}