package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class LogoutUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(): Flow<Result<Unit>> = accountRepository.logout()
}