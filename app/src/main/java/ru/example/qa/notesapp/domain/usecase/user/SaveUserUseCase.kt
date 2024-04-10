package ru.example.qa.notesapp.domain.usecase.user

import ru.example.qa.notesapp.domain.model.UserModel
import ru.example.qa.notesapp.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(username: String, email: String, password: String): UserModel {
        return userRepository.saveUser(
            UserModel(
                username = username,
                email = email,
                password = password,
            )
        )
    }
}