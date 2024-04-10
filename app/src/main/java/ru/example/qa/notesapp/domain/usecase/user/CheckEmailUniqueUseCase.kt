package ru.example.qa.notesapp.domain.usecase.user

import ru.example.qa.notesapp.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailUniqueUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): Boolean {
        return userRepository.checkEmailUnique(email)
    }
}