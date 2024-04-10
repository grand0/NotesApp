package ru.example.qa.notesapp.domain.usecase.user

import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserByCredentialsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(username: String, password: String): UserModel? {
        return userRepository.getUserByCredentials(username, password)
    }
}