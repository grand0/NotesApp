package ru.example.qa.notesapp.domain.usecase.user

import ru.example.qa.notesapp.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckUsernameUniqueUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(username: String): Boolean {
        return userRepository.checkUsernameUnique(username)
    }
}